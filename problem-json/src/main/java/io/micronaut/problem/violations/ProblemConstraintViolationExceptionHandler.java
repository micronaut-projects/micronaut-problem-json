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
package io.micronaut.problem.violations;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.validation.exceptions.ConstraintExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

/**
 * @author Sergio del Amo
 * @since 1.0
 */
@Produces
@Singleton
@Requires(classes = {ConstraintViolationException.class, ExceptionHandler.class})
@Replaces(ConstraintExceptionHandler.class)
public class ProblemConstraintViolationExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {
    private final ErrorResponseProcessor<?> responseProcessor;

    /**
     * Constructor.
     * @param responseProcessor Error Response Processor
     */
    public ProblemConstraintViolationExceptionHandler(ErrorResponseProcessor<?> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return responseProcessor.processResponse(
                ErrorContext.builder(request)
                        .cause(new ConstraintViolationThrowableProblem(new HttpStatusType(httpStatus),
                                exception.getConstraintViolations()
                                        .stream()
                                        .map(this::createViolation)
                                        .collect(Collectors.toList())))
                        .errorMessage(exception.getMessage())
                        .build(),
                HttpResponse.status(httpStatus)
        );
    }

    /**
     *
     * @param constraintViolation Constraint Violation
     * @return A Violation
     */
    @NonNull
    protected Violation createViolation(@NonNull ConstraintViolation<?> constraintViolation) {
        return new Violation(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
    }
}
