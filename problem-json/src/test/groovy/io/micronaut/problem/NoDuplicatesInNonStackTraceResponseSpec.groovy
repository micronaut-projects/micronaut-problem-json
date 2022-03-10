package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.exceptions.HttpClientResponseException

class NoDuplicatesInNonStackTraceResponseSpec extends EmbeddedServerSpecification {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + [
                'problem.stack-trace': false
        ]
    }

    @Override
    String getSpecName() {
        'NoDuplicatesInNonStackTraceResponseSpec'
    }

    def "properties are not duplicated with a no-stack-trace client error"() {
        when:
        def response = client.exchange('/client', String, String)

        then:
        HttpClientResponseException e = thrown()
        with(e.response.body()) { String it ->
            it.findAll('"type":').size() == 1
            it.findAll('"parameters":').size() == 1
            it.findAll('"detail":').size() == 1
            it.findAll('"status":').size() == 1
        }
    }

    @Requires(property = 'spec.name', value = 'NoDuplicatesInNonStackTraceResponseSpec')
    @Controller('/client')
    static class TaskController {

        @Get
        String index(@QueryValue String taskId) {
            return taskId;
        }
    }

}
