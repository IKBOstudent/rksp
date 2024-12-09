package com.example.pr7;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class CrudRxController {

    @Autowired
    CrudRepository rep;

    @GetMapping("/{id}")
    public Mono<Crud> get(@PathVariable int id) {
        return Mono.justOrEmpty(rep.findById(id));
    }

    @PostMapping
    public Mono<Crud> post(@RequestBody Crud v) {
        v.setId(0);
        return Mono.just(rep.save(v));
    }

    @PutMapping
    public Mono<Crud> update(@RequestBody Crud v) {
        Crud crud = rep.findById(v.getId()).orElse(null);
        if (crud == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        crud.setValue(v.getValue());
        return Mono.just(rep.save(crud));
    }

    @GetMapping("/all")
    public Flux<Crud> getAll() {
        return Flux.fromIterable(rep.findAll());
    }


    @PostMapping("/batch")
    public void batchCreate(@RequestBody List<Crud> cruds) {
        Flux.fromIterable(cruds)
                .map((v) -> {
                    v.setId(0);
                    return v;
                })
                .onErrorReturn(RuntimeException.class, new Crud(0, ""))
                .subscribe(new Subscriber<>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.s = subscription;
            }

            @Override
            public void onNext(Crud crud) {
                rep.save(crud);
                s.request(2);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError: " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        rep.deleteById(id);
    }
}
