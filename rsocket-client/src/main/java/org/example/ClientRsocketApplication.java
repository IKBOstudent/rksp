package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootApplication
public class ClientRsocketApplication implements CommandLineRunner {

    private final RSocketRequester rSocketRequester;

    public ClientRsocketApplication(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientRsocketApplication.class, args);
    }

    @Override
    public void run(String... args) {
        createCrud();
        readCrud();
        updateCrud();
        deleteCrud();
        readAllCruds();
        readCrudsByIds();

        System.exit(0);
    }

    private void createCrud() {
        Crud newCrud = new Crud(1, "new val");
        Mono<Crud> response = rSocketRequester.route("create")
                .data(Mono.just(newCrud))
                .retrieveMono(Crud.class);

        response.doOnNext(crud -> System.out.println("Created: " + crud))
                .block();
    }

    private void readCrud() {
        Mono<Crud> response = rSocketRequester.route("read")
                .data(Mono.just(1))
                .retrieveMono(Crud.class);

        response.doOnNext(crud -> System.out.println("Read: " + crud))
                .block();
    }

    private void updateCrud() {
        Crud updatedCrud = new Crud(1, "Updated Value");
        Mono<Void> response = rSocketRequester.route("update")
                .data(Mono.just(updatedCrud))
                .send();

        response.doOnTerminate(() -> System.out.println("Updated crud with id 1"))
                .block();
    }

    private void deleteCrud() {
        Mono<Void> response = rSocketRequester.route("delete")
                .data(Mono.just(1))
                .send();

        response.doOnTerminate(() -> System.out.println("Deleted crud with id 1"))
                .block();
    }

    private void readAllCruds() {
        Flux<Crud> response = rSocketRequester.route("readAll")
                .data(Mono.empty())
                .retrieveFlux(Crud.class);

        response.doOnNext(crud -> System.out.println("Read All: " + crud))
                .blockLast();
    }

    private void readCrudsByIds() {
        Flux<Crud> response = rSocketRequester.route("readIds")
                .data(Flux.just(1, 2, 3))
                .retrieveFlux(Crud.class);

        response.doOnNext(crud -> System.out.println("Read by IDs: " + crud))
                .blockLast();
    }

}
