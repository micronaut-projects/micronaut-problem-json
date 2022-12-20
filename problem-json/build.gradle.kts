plugins {
    id("io.micronaut.build.internal.problem-json-module")
}

dependencies {
    annotationProcessor(mnSerde.micronaut.serde.processor)
    api(libs.managed.zalando.problem)
    implementation(mnSerde.micronaut.serde.api)
    implementation(mn.micronaut.validation)
    implementation(mn.micronaut.http.server)
    implementation(mn.micronaut.jackson.databind)

    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testAnnotationProcessor(mn.micronaut.inject.java)
}
