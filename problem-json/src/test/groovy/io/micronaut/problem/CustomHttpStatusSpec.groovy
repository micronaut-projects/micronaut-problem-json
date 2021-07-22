package io.micronaut.problem

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.zalando.problem.Problem
import spock.lang.Issue


class CustomHttpStatusSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'CustomHttpStatusSpec'
    }

    @Issue('https://github.com/micronaut-projects/micronaut-problem-json/issues/32')
    void "response HTTP Status code should match the status of the Problem"() {
        when:
        client.exchange(HttpRequest.GET('/404'))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.NOT_FOUND
    }

    @Controller("/404")
    static class FourZeroFourController {

        @Get
        @Status(HttpStatus.OK)
        void index() {
            throw Problem.builder()
                    .withTitle("Not Found")
                    .withStatus(new HttpStatusType(HttpStatus.NOT_FOUND))
                    .withDetail("The requested entity was not found.")
                    .withType(URI.create( "https://example.com/errors/not-found"))
                    .build()
        }
    }
}
