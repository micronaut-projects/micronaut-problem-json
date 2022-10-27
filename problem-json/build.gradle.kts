plugins {
    id("io.micronaut.build.internal.problem-json-module")
}

dependencies {
    annotationProcessor(mn.micronaut.serde.processor)
    api(libs.managed.zalando.problem)
    implementation(mn.micronaut.serde.api)
    implementation(mn.micronaut.validation)
    implementation(mn.micronaut.http.server)

    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testAnnotationProcessor(mn.micronaut.inject.java)
}
