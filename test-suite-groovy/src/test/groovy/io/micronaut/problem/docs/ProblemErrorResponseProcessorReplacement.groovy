package io.micronaut.problem.docs

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.problem.ProblemJsonErrorResponseBodyProvider
import io.micronaut.problem.conf.ProblemConfiguration
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException
import jakarta.inject.Singleton
import org.jspecify.annotations.NonNull

@Requires(property = "spec.name", value = "DataLeakageOverrideSpec")
//tag::clazz[]
@Replaces(ProblemJsonErrorResponseBodyProvider)
@Singleton
class ProblemErrorResponseProcessorReplacement
        extends ProblemJsonErrorResponseBodyProvider {
    ProblemErrorResponseProcessorReplacement(ProblemConfiguration config) {
        super(config)
    }

    @Override
    protected boolean includeErrorMessage(@NonNull ErrorContext errorContext) {
        errorContext.rootCause
                .map(t -> t instanceof FooException || t instanceof UnsatisfiedRouteException)
                .orElse(false)
    }
}
//end::clazz[]
