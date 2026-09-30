package io.micronaut.problem.docs

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.problem.ProblemJsonErrorResponseBodyProvider
import io.micronaut.problem.conf.ProblemConfiguration
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException
import jakarta.inject.Singleton

@Requires(property = "spec.name", value = "DataLeakageOverrideTest")
//tag::clazz[]
@Replaces(ProblemJsonErrorResponseBodyProvider::class)
@Singleton
open class ProblemErrorResponseProcessorReplacement(config: ProblemConfiguration)
    : ProblemJsonErrorResponseBodyProvider(config) {

    override fun includeErrorMessage(errorContext: ErrorContext): Boolean {
        return errorContext.rootCause
            .map { t -> t is FooException || t is UnsatisfiedRouteException }
            .orElse(false)
    }
}
//end::clazz[]
