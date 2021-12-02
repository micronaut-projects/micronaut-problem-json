package io.micronaut.problem

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "spec.name", value = "TaskNotFoundProblemSpec")
@MicronautTest
class TaskNotFoundProblemSpec extends Specification {
    @Inject
    @Client("/")
    HttpClient httpClient

    void "custom problem is rendered"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET(UriBuilder.of('/task').path("3").build()), okArg, errorArg)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.NOT_FOUND
        e.response.contentType.isPresent()
        e.response.contentType.get().toString() == 'application/problem+json'

        when:
        Optional<Map> bodyOptional = e.response.getBody(errorArg)

        then:
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 4
        bodyOptional.get()['status'] == 404
        bodyOptional.get()['title'] == 'Not found'
        bodyOptional.get()['detail'] == "Task '3' not found"
        bodyOptional.get()['type'] == "https://example.org/not-found"
    }
}
