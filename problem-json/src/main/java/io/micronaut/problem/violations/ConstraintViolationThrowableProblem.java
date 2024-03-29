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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link ThrowableProblem} for constraints violations.
 * @author Sergio del Amo
 * @since 1.0
 */

@JsonIgnoreProperties(value = {"stackTrace", "localizedMessage", "message"})
@Serdeable
public class ConstraintViolationThrowableProblem extends ThrowableProblem {

    public static final String TYPE_VALUE = "https://zalando.github.io/problem/constraint-violation";
    public static final URI TYPE = URI.create(TYPE_VALUE);

    @NonNull
    private final URI type;

    @NonNull
    private final StatusType status;

    @NonNull
    private final List<Violation> violations;

    public ConstraintViolationThrowableProblem(@NonNull StatusType status,
                                               @Nullable List<Violation> violations) {
        this(TYPE, status, violations != null ? new ArrayList<>(violations) : new ArrayList<>());
    }

    public ConstraintViolationThrowableProblem(@NonNull URI type,
                                               @NonNull StatusType status,
                                               @Nullable List<Violation> violations) {
        this.type = type;
        this.status = status;
        this.violations = violations != null ? Collections.unmodifiableList(violations) : Collections.emptyList();
    }

    @Override
    @NonNull
    public URI getType() {
        return type;
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Constraint Violation";
    }

    @Override
    @NonNull
    public StatusType getStatus() {
        return status;
    }

    /**
     *
     * @return Violations
     */
    @NonNull
    public List<Violation> getViolations() {
        return violations;
    }
}
