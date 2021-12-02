package io.micronaut.problem

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

@MicronautTest
class OutOfStockSpec  extends Specification {
    @Inject
    @Client("/")
    HttpClient httpClient

    void "custom problem is rendered"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET(UriBuilder.of('/product').build()), okArg, errorArg)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST
        e.response.contentType.isPresent()
        e.response.contentType.get().toString() == 'application/problem+json'

        when:
        Optional<Map> bodyOptional = e.response.getBody(errorArg)

        then:
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 5
        bodyOptional.get()['status'] == 400
        bodyOptional.get()['title'] == 'Out of Stock'
        bodyOptional.get()['detail'] == "Item B00027Y5QG is no longer available"
        bodyOptional.get()['type'] == "https://example.org/out-of-stock"
        bodyOptional.get()['parameters'] == [product: "B00027Y5QG"]
    }
}
