package io.micronaut.problem;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "HeadRequestTest")
@Property(name = "micronaut.http.client.read-timeout", value = "10m")
@MicronautTest
public class HeadRequestTest {

    @Inject
    @Client("/")
    HttpClient httpClient;
    @Test
    void headRequestHasNoBodyNorContentType() {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();

        //when:
        Argument<?> okArg = Argument.of(String.class);
        Argument<?> errorArg = Argument.of(Map.class);

        //then:
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.HEAD(UriBuilder.of("/task").path("3").build()), okArg, errorArg)
        );

        assertFalse(e.getResponse().getContentType().isPresent());
        assertFalse(e.getResponse().getBody().isPresent());
    }

}
