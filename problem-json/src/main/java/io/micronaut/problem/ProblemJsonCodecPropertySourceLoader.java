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

import io.micronaut.context.env.ActiveEnvironment;
import io.micronaut.context.env.PropertySource;
import io.micronaut.context.env.PropertySourceLoader;
import io.micronaut.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * Property Source Locator which loads the {@link ProblemJsonCodecPropertySource}.
 * @author Sergio del Amo
 * @since 3.0.0
 */
public class ProblemJsonCodecPropertySourceLoader implements PropertySourceLoader {
    
    ProblemJsonCodecPropertySource propertySource = new ProblemJsonCodecPropertySource();

    @Override
    public Optional<PropertySource> load(String resourceName, ResourceLoader resourceLoader) {
        return Optional.of(propertySource);
    }

    @Override
    public Optional<PropertySource> loadEnv(String resourceName, ResourceLoader resourceLoader, ActiveEnvironment activeEnvironment) {
        return Optional.of(propertySource);
    }

    @Override
    public Map<String, Object> read(String name, InputStream input) throws IOException {
        return propertySource.asMap();
    }
}
