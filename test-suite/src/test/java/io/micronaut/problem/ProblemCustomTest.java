package io.micronaut.problem;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.zalando.problem.AbstractThrowableProblem;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "ProblemCustomTest")
@MicronautTest
class ProblemCustomTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void customProblemAreSerialized() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class,
            () -> httpClient.toBlocking().exchange(HttpRequest.GET("/product/problem"))
        );

        Map<String, Object> expected = CollectionUtils.mapOf(
            "type", "about:blank",
            "title", "Internal Server Error",
            "status", 500,
            "field", "random"
        );
        assertTrue(e.getResponse().getBody(Map.class).isPresent());
        Map<String, Object> m = e.getResponse().getBody(Map.class).get();
        assertEquals(expected.size(), m.keySet().size());
        assertEquals("about:blank", m.get("type"));
        assertEquals("Internal Server Error", m.get("title"));
        assertEquals(500, m.get("status"));
        assertEquals("random", m.get("field"));
    }

    @Requires(property = "spec.name", value = "ProblemCustomTest")
    @Controller("/product")
     static class ProductController {
        @Get("/problem")
        public void problem() {
            throw new ProductProblem("random");
        }

    }

    @Introspected
    static class ProductProblem extends AbstractThrowableProblem {

        private final String field;

        public ProductProblem(String field) {
            super(null, HttpStatus.INTERNAL_SERVER_ERROR.getReason(), new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }
}
