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

import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.http.HttpStatus;
import io.micronaut.serde.annotation.Serdeable;
import org.zalando.problem.StatusType;

/**
 * Implementation of {@link StatusType} which uses Micronaut {@link HttpStatus} and renders as JSON the HTTP status code.
 * @author Sergio del Amo
 * @since 1.0
 */
@Serdeable
public class HttpStatusType implements StatusType {

    private final HttpStatus httpStatus;

    public HttpStatusType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @JsonValue
    @Override
    public int getStatusCode() {
        return httpStatus.getCode();
    }

    @Override
    public String getReasonPhrase() {
        return httpStatus.getReason();
    }
}
