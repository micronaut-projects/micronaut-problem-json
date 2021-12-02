package io.micronaut.problem;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Map;
import java.util.Optional;

@Property(name = "spec.name", value = "TaskNotFoundProblemSpec")
@MicronautTest
public class TaskNotFoundProblemTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void customProblemIsRendered() {
        //given:
        BlockingHttpClient client = httpClient.toBlocking();
        //when:
        Argument<?> okArg = Argument.of(String.class);
        Argument<?> errorArg = Argument.of(Map.class);


        //then:
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.GET(UriBuilder.of("/task").path("3").build()), okArg, errorArg)
        );
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertTrue(e.getResponse().getContentType().isPresent());
        assertEquals("application/problem+json", e.getResponse().getContentType().get().toString());

        //when:
        Optional<Map> bodyOptional = e.getResponse().getBody(Map.class);

        //then:
        assertTrue(bodyOptional.isPresent());
        assertEquals(4, bodyOptional.get().keySet().size());
        assertEquals(404, bodyOptional.get().get("status"));
        assertEquals("Not found", bodyOptional.get().get("title"));
        assertEquals("Task '3' not found", bodyOptional.get().get("detail"));
        assertEquals("https://example.org/not-found", bodyOptional.get().get("type"));
    }
}
