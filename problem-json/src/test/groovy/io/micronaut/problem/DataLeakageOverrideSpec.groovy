package io.micronaut.problem

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.problem.conf.ProblemConfiguration
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException
import jakarta.inject.Singleton
import spock.lang.Issue

@Issue("https://github.com/micronaut-projects/micronaut-problem-json/issues/144")
class DataLeakageOverrideSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'DataLeakageOverrideSpec'
    }

    void "You can replace and override ProblemErrorResponseProcessorReplacement::shouldIncludeErrorMessageAsDetailForDefaultProblem"() {
        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET('/foo'), okArg, errorArg)

        then:
        HttpClientResponseException thrown = thrown()
        Optional<Map> bodyOptional = thrown.response.getBody(errorArg)
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
            throw new FooException("foo data");
        }
    }
}