/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.problem;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import org.zalando.problem.ThrowableProblem;

import jakarta.inject.Singleton;

/**
 * Handles exception of type {@link ThrowableProblem}.
 *
 * @author Sergio del Amo
 * @since 1.0
 */
@Produces
@Singleton
public class ThrowableProblemHandler implements ExceptionHandler<ThrowableProblem, HttpResponse<?>> {

    private final ErrorResponseProcessor<?> responseProcessor;

    /**
     * Constructor.
     * @param responseProcessor Error Response Processor
     */
    public ThrowableProblemHandler(ErrorResponseProcessor<?> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    @Override
    public HttpResponse<?> handle(HttpRequest request, ThrowableProblem exception) {
        return responseProcessor.processResponse(
                ErrorContext.builder(request)
                        .cause(exception)
                        .errorMessage(exception.getMessage())
                        .build(),
                HttpResponse.status(exception.getStatus() == null ? HttpStatus.BAD_REQUEST :
                        HttpStatus.valueOf(exception.getStatus().getStatusCode()))
        );
    }
}
