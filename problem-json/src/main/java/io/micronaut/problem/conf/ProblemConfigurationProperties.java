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
 *
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
    public static final boolean DEFAULT_STACK_TRACE = false;

    /**
     * The default stackTrace value.
     *
     * @deprecated for removal in Micronaut 4 or later. Use {@link #DEFAULT_STACK_TRACE} instead
     */
    @SuppressWarnings("WeakerAccess")
    @Deprecated
    public static final boolean DEFAULT_STACK_TRACKE = DEFAULT_STACK_TRACE;

    /**
     * The default isDetail value.
     * <p>
     * {@code True} until Micronaut 4 at which time the default will be changed to {@code false} which is a breaking change.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_DETAIL = true;

    private boolean enabled = DEFAULT_ENABLED;

    private boolean stackTrace = DEFAULT_STACK_TRACE;

    private boolean detail = DEFAULT_DETAIL;

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
     * @param stackTrace Whether the HTTP Response should include the stack trace for instances of {@link org.zalando.problem.ThrowableProblem}. Default value ({@value #DEFAULT_STACK_TRACE}).
     */
    public void setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public boolean isDetail() {
        return detail;
    }

    /**
     * @param detail Whether the HTTP Response should include the detailed, human-readable explanation of problems that are
     *               instances of {@link org.zalando.problem.ThrowableProblem}.  See {@link #DEFAULT_DETAIL} regarding the default value.
     */
    public void setDetail(boolean detail) {
        this.detail = detail;
    }
}
