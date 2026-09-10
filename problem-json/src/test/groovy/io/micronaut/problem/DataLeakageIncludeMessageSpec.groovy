package io.micronaut.problem

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.exceptions.HttpClientResponseException

class DataLeakageIncludeMessageSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'DataLeakageSpec'
    }

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': specName, 'micronaut.server.error-response-include-message': 'always']
    }

    void "the detail is omitted even when the server includes the exception message in the error message"() {
        when:
        client.exchange(HttpRequest.GET('/foo'), Argument.of(String), Argument.of(Map))

        then:
        HttpClientResponseException thrown = thrown()
        Optional<Map> bodyOptional = thrown.response.getBody(Argument.of(Map))
        bodyOptional.isPresent()
        bodyOptional.get().keySet() == ['type', 'status'] as Set
        bodyOptional.get()['status'] == 500
    }
}
