package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder

class TaskNotFoundProblemWithStackTraceSpec extends EmbeddedServerSpecification {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + [
            'problem.stack-trace': true
        ]
    }

    @Override
    String getSpecName() {
        'TaskNotFoundProblemWithStackTraceSpec'
    }

    void "custom problem is rendered"() {
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
        bodyOptional.get().keySet().size() == 5
        bodyOptional.get().keySet().contains('stackTrace')
        bodyOptional.get()['status'] == 404
        bodyOptional.get()['title'] == 'Not found'
        bodyOptional.get()['detail'] == "Task '3' not found"
        bodyOptional.get()['type'] == "https://example.org/not-found"
    }

    @Requires(property = 'spec.name', value = 'TaskNotFoundProblemWithStackTraceSpec')
    @Controller('/task')
    static class TaskController {

        @Get('/{taskId}')
        @Status(HttpStatus.OK)
        void index(@PathVariable Long taskId) {
            throw new TaskNotFoundProblem(taskId);
        }
    }
}
