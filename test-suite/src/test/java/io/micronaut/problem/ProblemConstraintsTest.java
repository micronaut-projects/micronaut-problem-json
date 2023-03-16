package io.micronaut.problem;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "ProblemConstraintsTest")
@MicronautTest
public class ProblemConstraintsTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void thatViolationsAreSerializedOnConstraintViolations() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class,
            () -> httpClient.toBlocking().exchange(HttpRequest.POST("/product", new Widget()))
        );

        Map<String, Object> expected = CollectionUtils.mapOf(
        "type", "https://zalando.github.io/problem/constraint-violation",
            "title", "Constraint Violation",
            "status", 400,
            "violations", Collections.singletonList(CollectionUtils.mapOf("field", "add.item.item", "message", "must not be blank"))
        );
        assertTrue(e.getResponse().getBody(Map.class).isPresent());
        Map<String, Object> m = e.getResponse().getBody(Map.class).get();
        assertEquals(expected.size(), m.size());

        assertEquals("https://zalando.github.io/problem/constraint-violation", m.get("type"));
        assertEquals("Constraint Violation", m.get("title"));
        assertEquals(400, m.get("status"));
        assertEquals(Collections.singletonList(CollectionUtils.mapOf("field", "add.item.item", "message", "must not be blank")), m.get("violations"));
    }

    @Introspected
    static class Widget {
        @NotBlank
        String item;

        public String getItem() {
            return item;
        }
    }

    @Requires(property = "spec.name", value = "ProblemConstraintsTest")
    @Controller("/product")
    static class ProblemController {
        @Post
        public HttpResponse<String> add(@Valid @Body Widget item) {
            return HttpResponse.ok(item.getItem());
        }
    }
}
