/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;

import java.util.Collections;

/**
 * {@link ContextConfigurer} which registers application/problem+json media type as an additional type of the JSON codec.
 * @author Sergio del Amo
 * @since 3.0.0
 */
@ContextConfigurer
public class ProblemJsonCodecApplicationContextConfigurer implements ApplicationContextConfigurer {

    @Override
    public void configure(ApplicationContextBuilder builder) {
        builder.properties(Collections.singletonMap("micronaut.codec.json.additional-types", Collections.singletonList("application/problem+json")));
    }
}
