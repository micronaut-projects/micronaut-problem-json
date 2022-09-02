plugins {
    id("io.micronaut.build.internal.module")
}

dependencies {
    //Don't pin versions once Micronaut 3.6.2 is released
    //annotationProcessor(mn.micronaut.serde.processor)
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor:1.3.2")
    api(libs.managed.zalando.problem)
    //implementation(mn.micronaut.serde.api)
    implementation("io.micronaut.serde:micronaut-serde-api:1.3.2")
    implementation(mn.micronaut.validation)
    implementation(mn.micronaut.http.server)

    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mn.micronaut.http.client)
    testAnnotationProcessor(mn.micronaut.inject.java)
}
