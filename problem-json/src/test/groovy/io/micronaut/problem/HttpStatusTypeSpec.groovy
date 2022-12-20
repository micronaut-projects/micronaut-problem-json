package io.micronaut.problem

import io.micronaut.http.HttpStatus
import io.micronaut.serde.ObjectMapper
import io.micronaut.serde.annotation.Serdeable
import org.zalando.problem.StatusType
import spock.lang.Shared

class HttpStatusTypeSpec extends ApplicationContextSpecification {

    @Shared
    ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper)


    void "HttpStatusType when render as JSON is the number code"() {
        given:
        MockHttpStatusType pojo = new MockHttpStatusType(status: new HttpStatusType(HttpStatus.I_AM_A_TEAPOT))

        expect:
        objectMapper.writeValueAsString(pojo) == '{"status":418}'
    }

    @Serdeable
    static class MockHttpStatusType {
        StatusType status
    }
}
