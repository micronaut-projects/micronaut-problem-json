package io.micronaut.problem

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.exceptions.HttpClientResponseException

class DataLeakageOverrideIncludeMessageSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'DataLeakageOverrideSpec'
    }

    @Override
    Map<String, Object> getConfiguration() {
        ['spec.name': specName, 'micronaut.server.error-response-include-message': 'always']
    }

    void "the exception message is not repeated when the server already includes it in the error message"() {
        when:
        client.exchange(HttpRequest.GET('/foo'), Argument.of(String), Argument.of(Map))

        then:
        HttpClientResponseException thrown = thrown()
        Optional<Map> bodyOptional = thrown.response.getBody(Argument.of(Map))
        bodyOptional.isPresent()
        bodyOptional.get()['status'] == 500
        bodyOptional.get()['detail'] == "Internal Server Error: foo data"
    }
}
