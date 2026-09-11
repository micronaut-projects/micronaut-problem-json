package io.micronaut.problem

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.problem.conf.ProblemConfiguration
import spock.lang.Specification

class DefaultProblemDetailSpec extends Specification {

    ProblemJsonErrorResponseBodyProvider provider = new ProblemJsonErrorResponseBodyProvider(Stub(ProblemConfiguration)) {
        @Override
        protected boolean includeErrorMessage(ErrorContext errorContext) {
            true
        }
    }

    void "the detail of error '#message' with cause #cause and status #status is '#detail'"() {
        given:
        ErrorContext errorContext = ErrorContext.builder(HttpRequest.GET('/foo'))
                .cause(cause)
                .errorMessage(message)
                .build()

        expect:
        provider.defaultProblem(errorContext, status).detail == detail

        where:
        message                           | cause                           | status                           || detail
        'Internal Server Error'           | new RuntimeException('foo data') | HttpStatus.INTERNAL_SERVER_ERROR || 'Internal Server Error: foo data'
        'Internal Server Error: foo data' | new RuntimeException('foo data') | HttpStatus.INTERNAL_SERVER_ERROR || 'Internal Server Error: foo data'
        'Internal Server Error'           | new RuntimeException()           | HttpStatus.INTERNAL_SERVER_ERROR || 'Internal Server Error'
        'Internal Server Error'           | null                             | HttpStatus.INTERNAL_SERVER_ERROR || 'Internal Server Error'
        'Internal Server Error'           | new RuntimeException('foo data') | HttpStatus.BAD_REQUEST           || 'Internal Server Error'
        'Bad Request'                     | new RuntimeException('foo data') | HttpStatus.BAD_REQUEST           || 'Bad Request'
    }
}
