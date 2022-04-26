package io.micronaut.problem


import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.exceptions.HttpClientResponseException

class DataLeakageSpec extends EmbeddedServerSpecification {
    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + [
                'problem.stack-trace': false,
                'problem.detail'     : false
        ]
    }

    @Override
    String getSpecName() {
        'DataLeakageSpec'
    }

    void "No unintended data leakage in instances of Problem"() {
        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.GET('/foo'), okArg, errorArg)

        then:
        HttpClientResponseException thrown = thrown()
        Optional<Map> bodyOptional = thrown.response.getBody(errorArg)
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 2
        bodyOptional.get()['status'] == 500
        bodyOptional.get()['type'] == "about:blank"
    }

    @Controller('/foo')
    static class FooController {

        @Get
        void doSomething() {
            throw new Exception("foo data");
        }
    }
}