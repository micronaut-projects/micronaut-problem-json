package io.micronaut.problem.docs

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "spec.name", value = "DataLeakageOverrideSpec")
@MicronautTest
class DataLeakageOverrideSpec extends Specification {
    @Inject
    @Client("/")
    HttpClient httpClient

    void "the replaced provider includes the error message as detail"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET('/foo'), okArg, errorArg)

        then:
        HttpClientResponseException e = thrown()
        Optional<Map> bodyOptional = e.response.getBody(errorArg)
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 3
        bodyOptional.get()['status'] == 500
        bodyOptional.get()['type'] == "about:blank"
        bodyOptional.get()['detail'] == "Internal Server Error: foo data"
    }

    @Requires(property = "spec.name", value = "DataLeakageOverrideSpec")
    @Controller('/foo')
    static class FooController {
        @Get
        void doSomething() {
            throw new FooException("foo data")
        }
    }
}
