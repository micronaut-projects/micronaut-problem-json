plugins {
    id("io.micronaut.build.internal.problem-json-module")
}

dependencies {
    annotationProcessor(mnSerde.micronaut.serde.processor)
    annotationProcessor(mnValidation.micronaut.validation.processor)

    api(libs.managed.zalando.problem)
    api(mnSerde.micronaut.serde.api)
    api(mn.jackson.annotations)
    implementation(mnValidation.micronaut.validation)
    implementation(mn.micronaut.http.server)

    testAnnotationProcessor(mn.micronaut.inject.java)

    testImplementation(mnValidation.micronaut.validation.processor)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mnSerde.micronaut.serde.jackson)
}
