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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.Error;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;

import jakarta.inject.Singleton;

/**
 * Error Response processor to respond {@link Problem} responses.
 * <ul>
 *   <li>Adds application/problem+json content type</li>
 *   <li>If the cause is a {@link ThrowableProblem}, it returns as the body.</li>
 *   <li>If the cause is not a {@link ThrowableProblem} , it generates a default Problem based on the {@link ErrorContext} and returns it as the HTTP Body.</li>
 * </ul>
 * @author Sergio del Amo
 * @since 1.0
 */
@Singleton
public class ProblemErrorResponseProcessor implements ErrorResponseProcessor<Problem> {
    public static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    @Override
    @NonNull
    public MutableHttpResponse<Problem> processResponse(@NonNull ErrorContext errorContext,
                                                        @NonNull MutableHttpResponse<?> baseResponse) {
        return baseResponse
                .contentType(APPLICATION_PROBLEM_JSON)
                .body((errorContext.getRootCause().isPresent() && errorContext.getRootCause().get() instanceof ThrowableProblem) ?
                        ((ThrowableProblem) errorContext.getRootCause().get()) : defaultProblem(errorContext, baseResponse.getStatus()));
    }

    /**
     *
     * @param errorContext Error Context
     * @param httpStatus HTTP Status
     * @return Default problem
     */
    @NonNull
    protected ThrowableProblem defaultProblem(@NonNull ErrorContext errorContext,
                                              @NonNull HttpStatus httpStatus) {
        org.zalando.problem.ProblemBuilder problemBuilder = Problem.builder().withStatus(new HttpStatusType(httpStatus));
        if (!errorContext.getErrors().isEmpty()) {
            Error error = errorContext.getErrors().get(0);
            if (error.getTitle().isPresent()) {
                problemBuilder.withTitle(error.getTitle().get());
            }
            problemBuilder.withDetail(error.getMessage());
            if (error.getPath().isPresent()) {
                problemBuilder.with("path", error.getPath().get());
            }
        }
        return problemBuilder.build();
    }
}
