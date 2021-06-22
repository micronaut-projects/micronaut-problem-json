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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import org.zalando.problem.ThrowableProblem;

import jakarta.inject.Singleton;

/**
 * If {@link ProblemConfiguration#isStackTrace()} returns false adds the mixin {@link MixInThrowableProblem} for class {@link ThrowableProblem} to the {@link ObjectMapper}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Singleton
public class ThrowableProblemObjectMapperBeanCreatedEventListener implements BeanCreatedEventListener<ObjectMapper> {

    private final ProblemConfiguration problemConfiguration;

    /**
     *
     * @param problemConfiguration Problem Configuration
     */
    public ThrowableProblemObjectMapperBeanCreatedEventListener(ProblemConfiguration problemConfiguration) {
        this.problemConfiguration = problemConfiguration;
    }

    @Override
    public ObjectMapper onCreated(BeanCreatedEvent<ObjectMapper> event) {
        if (!problemConfiguration.isStackTrace()) {
            event.getBean().addMixIn(ThrowableProblem.class, MixInThrowableProblem.class);
        }
        return event.getBean();
    }
}
