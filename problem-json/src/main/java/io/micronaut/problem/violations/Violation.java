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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;

/**
 * Constraint Violation information.
 * @author Sergio del Amo
 * @since 1.0
 */
@Serdeable
public class Violation {
    /**
     * Field affected by the violation.
     */
    @NonNull
    @NotBlank
    private String field;

    /**
     * Message explaining the violation.
     */
    @NonNull
    @NotBlank
    private String message;

    /**
     *
     * @param field Field affected by the violation
     * @param message message explaining the violation.
     */
    public Violation(@NonNull String field, @NonNull String message) {
        this.field = field;
        this.message = message;
    }

    /**
     *
     * @return Field affected by the violation
     */
    @NonNull
    public String getField() {
        return field;
    }

    /**
     *
     * @param field Field affected by the violation
     */
    public void setField(@NonNull String field) {
        this.field = field;
    }

    /**
     *
     * @return message explaining the violation.
     */
    @NonNull
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message message explaining the violation.
     */
    public void setMessage(@NonNull String message) {
        this.message = message;
    }
}
