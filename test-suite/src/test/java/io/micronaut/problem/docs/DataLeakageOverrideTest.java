package io.micronaut.problem.docs;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "DataLeakageOverrideTest")
@MicronautTest
class DataLeakageOverrideTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void replacedProviderIncludesTheErrorMessageAsDetail() {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();
        Argument<?> okArg = Argument.of(String.class);
        Argument<?> errorArg = Argument.of(Map.class);

        //when:
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            client.exchange(HttpRequest.GET("/foo"), okArg, errorArg)
        );

        //then:
        Optional<Map> bodyOptional = e.getResponse().getBody(Map.class);
        assertTrue(bodyOptional.isPresent());
        assertEquals(3, bodyOptional.get().keySet().size());
        assertEquals(500, bodyOptional.get().get("status"));
        assertEquals("about:blank", bodyOptional.get().get("type"));
        assertEquals("Internal Server Error: foo data", bodyOptional.get().get("detail"));
    }

    @Requires(property = "spec.name", value = "DataLeakageOverrideTest")
    @Controller("/foo")
    static class FooController {
        @Get
        void doSomething() {
            throw new FooException("foo data");
        }
    }
}
