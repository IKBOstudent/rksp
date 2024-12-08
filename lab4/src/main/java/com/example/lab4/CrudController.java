package com.example.lab4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class CrudController {

    @Autowired
    CrudRepository rep;

    // 1. Request-Response method
    @MessageMapping("create")
    public Mono<Crud> create(Mono<Crud> crud) {
        return crud.map(rep::save);
    }

    // 1. Request-Response method
    @MessageMapping("read")
    public Mono<Crud> read(Mono<Integer> id) {
        return id.flatMap((v) -> Mono.justOrEmpty(rep.findById(v)));
    }

    // 2. Fire-and-Forget method
    @MessageMapping("update")
    @Transactional
    public Mono<Void> update(Mono<Crud> crud) {
        crud.subscribe((v) -> {
            Crud ref = rep.getReferenceById(v.getId());
            ref.setValue(v.getValue());
            rep.save(ref);
        });
        return Mono.empty();
    }

    // 2. Fire-and-Forget method
    @MessageMapping("delete")
    public Mono<Void> delete(Mono<Integer> id) {
        id.subscribe(rep::deleteById);
        return Mono.empty();
    }

    // 3. Request-Stream method
    @MessageMapping("readAll")
    public Flux<Crud> readAll(Mono<Integer> id) {
        return Flux.fromIterable(rep.findAll());
    }

    // 4. Channel method
    @MessageMapping("readIds")
    public Flux<Crud> readIds(Flux<Integer> ids) {
        return ids.bufferTimeout(1000, Duration.ofMillis(100))
                .map((curIds) -> rep.findAllById(curIds))
                .flatMap(Flux::fromIterable);
    }


}
