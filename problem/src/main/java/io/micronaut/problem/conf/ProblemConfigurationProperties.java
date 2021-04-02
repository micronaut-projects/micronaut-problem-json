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

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} implementation of {@link ProblemConfiguration}.
 * @author Sergio del Amo
 * @since 1.0
 */
@ConfigurationProperties(ProblemConfigurationProperties.PREFIX)
public class ProblemConfigurationProperties implements ProblemConfiguration {

    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "problem";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    /**
     * The default stackTrace value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_STACK_TRACKE = false;

    private boolean enabled = DEFAULT_ENABLED;

    private boolean stackTrace = DEFAULT_STACK_TRACKE;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the configuration is enabled. Default value {@value #DEFAULT_ENABLED}.
     *
     * @param enabled True if it is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isStackTrace() {
        return stackTrace;
    }

    /**
     *
     * @param stackTrace Whether the HTTP Response should include the stack trace for instances of {@link org.zalando.problem.ThrowableProblem}. Default value ({@value #DEFAULT_STACK_TRACKE}).
     */
    public void setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
    }
}
