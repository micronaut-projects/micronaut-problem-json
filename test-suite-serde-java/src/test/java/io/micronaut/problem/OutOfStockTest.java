package io.micronaut.problem;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class OutOfStockTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    ObjectMapper objectMapper;

    @Test
    void problemSerializedWithSerde() throws IOException {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();

        //when:
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.GET(UriBuilder.of("/product").build()))
        );

        //then:
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertTrue(e.getResponse().getContentType().isPresent());
        assertEquals("application/problem+json", e.getResponse().getContentType().get().toString());
    }

    @Disabled
    @Test
    void ProblemIsRenderedToMapWithSerde() throws IOException {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();
        //when:
        Argument<?> okArg = Argument.of(String.class);
        Argument<?> errorArg = Argument.of(Map.class);
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.GET(UriBuilder.of("/product").build()), okArg, errorArg)
        );

        //then:
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertTrue(e.getResponse().getContentType().isPresent());
        assertEquals("application/problem+json", e.getResponse().getContentType().get().toString());

        //when:
        Optional<Map> bodyOptional = e.getResponse().getBody(Map.class);

        //then:
        assertNotNull(bodyOptional);
        assertTrue(bodyOptional.isPresent());

        //and:
        assertEquals(5, bodyOptional.get().keySet().size());
        assertEquals(400, bodyOptional.get().get("status"));
        assertEquals("Out of Stock", bodyOptional.get().get("title"));
        assertEquals("Item B00027Y5QG is no longer available", bodyOptional.get().get("detail"));
        assertEquals("https://example.org/out-of-stock", bodyOptional.get().get("type"));
        assertEquals(Collections.singletonMap("product", "B00027Y5QG"), bodyOptional.get().get(("parameters")));
    }

    @Test
    void ProblemIsRenderedToStringWithSerde() throws IOException {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();
        //when:
        Argument<?> okArg = Argument.of(String.class);
        Argument<?> errorArg = Argument.of(String.class);
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.GET(UriBuilder.of("/product").build()), okArg, errorArg)
        );

        //then:
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertTrue(e.getResponse().getContentType().isPresent());
        assertEquals("application/problem+json", e.getResponse().getContentType().get().toString());

        //when:
        Optional<String> bodyOptional = e.getResponse().getBody(String.class);

        //then:
        assertNotNull(bodyOptional);
        assertTrue(bodyOptional.isPresent());

        //and:
        assertEquals("{\"type\":\"https://example.org/out-of-stock\",\"title\":\"Out of Stock\",\"status\":{\"statusCode\":400,\"reasonPhrase\":\"Bad Request\"},\"detail\":\"Item B00027Y5QG is no longer available\",\"parameters\":{\"product\":\"B00027Y5QG\"}}", bodyOptional.get());
    }
}
