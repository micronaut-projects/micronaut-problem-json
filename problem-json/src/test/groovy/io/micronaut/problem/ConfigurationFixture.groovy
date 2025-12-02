package io.micronaut.problem

import org.jspecify.annotations.NonNull
import org.jspecify.annotations.Nullable

interface ConfigurationFixture {

    @NonNull
    default Map<String, Object> getConfiguration() {
        Map<String, Object> m = [:]
        if (specName) {
            m['spec.name'] = specName
        }
        m
    }

    @Nullable
    default String getSpecName() {
        null
    }
}
