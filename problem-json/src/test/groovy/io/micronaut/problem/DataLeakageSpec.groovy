package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.exceptions.HttpClientResponseException
import spock.lang.Issue
import spock.lang.Narrative

@Issue("https://github.com/micronaut-projects/micronaut-problem-json/issues/144")
@Narrative("""\
Exceptions that do not extend ThrowableProblem should not include Exception::getMessage in its details part as it 
may carry sensitive information and cause information disclosure.  
""")
class DataLeakageSpec extends EmbeddedServerSpecification {

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

    @Requires(property = "spec.name", value = "DataLeakageSpec")
    @Controller('/foo')
    static class FooController {

        @Get
        void doSomething() {
            throw new Exception("foo data");
        }
    }
}