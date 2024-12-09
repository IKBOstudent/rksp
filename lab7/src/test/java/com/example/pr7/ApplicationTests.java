package com.example.pr7;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApplicationTests {
	static WebClient client;

	@BeforeAll
    static void init() {
		client = WebClient.create("http://127.0.0.1:8082/");
	}

	@Test
	void contextLoads() {
	}

	@Test
	void read() {
		Crud v = client.get()
				.uri(Integer.toString(Integer.MAX_VALUE))
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

        assertNull(v);
	}

	@Test
	void create() {
		Crud crud = client.post()
				.uri("/")
				.body(Mono.just(Crud.builder().value("test value").build()), Crud.class)
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertNotNull(crud);


		Crud v = client.get()
				.uri(Integer.toString(crud.getId()))
				.retrieve()
				.bodyToMono(Crud.class)
				.block();


		assertEquals(crud, v);
	}

	@Test
	void update() {
		Crud crud = client.post()
				.uri("/")
				.body(Mono.just(Crud.builder().value("test value").build()), Crud.class)
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertNotNull(crud);

		client.put()
				.uri("/")
				.body(Mono.just(Crud.builder().id(crud.getId()).value("different value").build()), Crud.class)
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		Crud v = client.get()
				.uri(Integer.toString(crud.getId()))
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertEquals("test value", crud.getValue());
		assertEquals("different value", v.getValue());
	}

	@Test
	void delete() {
		Crud crud = client.post()
				.uri("/")
				.body(Mono.just(Crud.builder().value("test value").build()), Crud.class)
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertNotNull(crud);
		Crud v1 = client.get()
				.uri(Integer.toString(crud.getId()))
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertEquals(crud, v1);
		client.delete()
				.uri(Integer.toString(crud.getId()))
				.retrieve()
				.toBodilessEntity()
				.block();

		Crud v2 = client.get()
				.uri(Integer.toString(crud.getId()))
				.retrieve()
				.bodyToMono(Crud.class)
				.block();

		assertNull(v2);
	}

	@Test
	void readAll() {
		Flux<Crud> data = client.get()
				.uri("all")
				.retrieve()
				.bodyToFlux(Crud.class);

        assertFalse(data.collectList().block().isEmpty());
	}

	@Test
	void batchCreate() {
		List<Crud> cruds = IntStream.range(0, 100)
				.mapToObj((v) -> Crud.builder().value(Integer.toString(v)).build())
				.toList();

		client.post()
				.uri("/")
				.body(Flux.fromIterable(cruds), Crud.class)
				.retrieve();
	}
}
