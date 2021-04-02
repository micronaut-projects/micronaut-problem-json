package io.micronaut.problem

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

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
