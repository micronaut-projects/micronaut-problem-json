package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.uri.UriBuilder
import org.zalando.problem.Problem
import spock.lang.Ignore

@Ignore
class HttpStatusExceptionSpec extends EmbeddedServerSpecification {
    @Override
    String getSpecName() {
        'HttpStatusExceptionSpec'
    }

    void "problem json is rendered for HttpStatusException"() {
        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET(UriBuilder.of('/httpstatus').build()), okArg, errorArg)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.I_AM_A_TEAPOT
        e.response.contentType.isPresent()
        e.response.contentType.get().toString() == 'application/problem+json'

        when:
        Optional<Map> bodyOptional = e.response.getBody(errorArg)

        then:
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 3
        bodyOptional.get()['status'] == 418
        bodyOptional.get()['title'] == "I am a teapot"
        bodyOptional.get()['type'] == Problem.DEFAULT_TYPE
    }

    @Requires(property = 'spec.name', value = 'HttpStatusExceptionSpec')
    @Controller('/httpstatus')
    static class HttpStatusTaskController {

        @Get
        @Status(HttpStatus.OK)
        void index() {

            throw new HttpStatusException(HttpStatus.I_AM_A_TEAPOT, "I am a teapot");
        }
    }
}
