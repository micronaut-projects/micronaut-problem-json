package io.micronaut.problem;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.problem.conf.ProblemConfiguration;
import io.micronaut.problem.violations.ConstraintViolationThrowableProblem;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "ProblemSerdeTest")
@MicronautTest
public class ProblemSerdeTest {
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
        assertEquals("{\"type\":\"https://example.org/out-of-stock\",\"title\":\"Out of Stock\",\"status\":400,\"detail\":\"Item B00027Y5QG is no longer available\",\"parameters\":{\"product\":\"B00027Y5QG\"}}", bodyOptional.get());
    }

    /*
        The docs say, "The default Problem+JSON payload does not include the detail field to avoid
            accidental information disclosure if the exception root cause is not of type
            UnsatisfiedRouteException or ThrowableProblem"
        However, see ProblemErrorResponseProcessor.includeErrorMessage(). It only handles UnsatisfiedRouteException
        and not ThrowableProblem.

        The docs at https://micronaut-projects.github.io/micronaut-problem-json/latest/guide/#customizingProblemErrorResponseProcessor
        makes me think the following ProblemErrorResponseProcessorReplacement should work,
        but includeErrorMessage() is never called.
     */
    @Test
    @Disabled("pending fix for https://github.com/micronaut-projects/micronaut-problem-json/issues/176")
    void thatViolationsAreSerializedOnConstraintViolations() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class,
            () -> httpClient.toBlocking().exchange(HttpRequest.POST("/product", new Widget()))
        );

        String expected = "{\"type\":\"https://zalando.github.io/problem/constraint-violation\",\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"add.item.item\",\"message\":\"must not be blank\"}]}";
        assertTrue(e.getResponse().getBody(String.class).isPresent());
        assertEquals(expected, e.getResponse().getBody(String.class).get());
    }

    @Test
    @Disabled("pending fix for https://github.com/micronaut-projects/micronaut-problem-json/issues/176")
    public void testFooController() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class,
            () -> httpClient.toBlocking().exchange(HttpRequest.GET("/product/problem"))
        );

        String expected = "{\"type\":\"about:blank\",\"title\":\"Internal Server Error\",\"status\":500,\"field\":\"random\"}";
        assertTrue(e.getResponse().getBody(String.class).isPresent());
        assertEquals(expected, e.getResponse().getBody(String.class).get());

    }
}

@Requires(property = "spec.name", value = "ProblemSerdeTest")
@Replaces(ProblemErrorResponseProcessor.class)
@Singleton
class ProblemErrorResponseProcessorReplacement extends ProblemErrorResponseProcessor {
    ProblemErrorResponseProcessorReplacement(ProblemConfiguration config) {
        super(config);
    }

    @Override
    protected boolean includeErrorMessage(@NonNull ErrorContext errorContext) {
        boolean defaultIncludes = super.includeErrorMessage(errorContext);
        Optional<Throwable> rootCause = errorContext.getRootCause();
        return rootCause
            .map(t -> defaultIncludes ||
                t instanceof ConstraintViolationThrowableProblem ||
                t instanceof ProductProblem
            )
            .orElse(false);
    }
}
