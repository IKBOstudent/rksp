package com.example.lab4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CrudControllerTests {

    @Mock
    private CrudRepository rep;

    @InjectMocks
    private CrudController crudController;

    private Crud sampleCrud;

    @BeforeEach
    public void setUp() {
        sampleCrud = new Crud();
        sampleCrud.setId(1);
        sampleCrud.setValue("test value");
    }

    @Test
    public void testCreate() {
        when(rep.save(any(Crud.class))).thenReturn(sampleCrud);

        Mono<Crud> resultMono = crudController.create(Mono.just(sampleCrud));

        StepVerifier.create(resultMono)
                .expectNext(sampleCrud)
                .verifyComplete();
    }

    @Test
    public void testRead() {
        when(rep.findById(1)).thenReturn(Optional.of(sampleCrud));

        Mono<Crud> resultMono = crudController.read(Mono.just(1));

        StepVerifier.create(resultMono)
                .expectNext(sampleCrud)
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        when(rep.getReferenceById(1)).thenReturn(sampleCrud);
        when(rep.save(any(Crud.class))).thenReturn(sampleCrud);

        Mono<Void> resultMono = crudController.update(Mono.just(sampleCrud));

        StepVerifier.create(resultMono)
                .verifyComplete();

        verify(rep, times(1)).save(sampleCrud);
    }

    @Test
    public void testDelete() {
        Mono<Void> resultMono = crudController.delete(Mono.just(1));

        StepVerifier.create(resultMono)
                .verifyComplete();

        verify(rep, times(1)).deleteById(1);
    }

    @Test
    public void testReadAll() {
        when(rep.findAll()).thenReturn(List.of(sampleCrud));

        Flux<Crud> resultFlux = crudController.readAll(Mono.just(1));

        StepVerifier.create(resultFlux)
                .expectNext(sampleCrud)
                .verifyComplete();
    }

    @Test
    public void testReadIds() {
        when(rep.findAllById(List.of(1))).thenReturn(List.of(sampleCrud));

        Flux<Crud> resultFlux = crudController.readIds(Flux.just(1));

        StepVerifier.create(resultFlux)
                .expectNext(sampleCrud)
                .verifyComplete();
    }
}