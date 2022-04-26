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
package io.micronaut.problem.conf;


import io.micronaut.core.util.Toggleable;

/**
 * Configuration for problem.
 *
 * @author Sergio del Amo
 * @since 1.0.0
 */
public interface ProblemConfiguration extends Toggleable {

    /**
     * @return Whether the HTTP Response should include the stack trace for instances of {@link org.zalando.problem.ThrowableProblem}
     */
    boolean isStackTrace();

    /**
     * @return Whether the HTTP Response should include the detailed, human-readable explanation of problems that are
     * instances of {@link org.zalando.problem.ThrowableProblem}
     */
    default boolean isDetail() {
        return false;
    }
}
