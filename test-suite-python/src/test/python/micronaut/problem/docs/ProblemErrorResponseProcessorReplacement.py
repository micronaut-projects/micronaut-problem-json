from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces, Requires
from micronaut.http.server.exceptions.response import ErrorContext
from micronaut.problem import ProblemJsonErrorResponseBodyProvider
from micronaut.problem.conf import ProblemConfiguration
from micronaut.web.router.exceptions import UnsatisfiedRouteException

from .FooException import FooException


@Requires(property="spec.name", value="DataLeakageOverrideTest")
# tag::clazz[]
@Replaces(ProblemJsonErrorResponseBodyProvider)
@Singleton
class ProblemErrorResponseProcessorReplacement(ProblemJsonErrorResponseBodyProvider):

    def __init__(self, config: ProblemConfiguration):
        super().__init__(config)

    def includeErrorMessage(self, error_context: ErrorContext) -> bool:
        return (error_context.getRootCause()
                .map(lambda t: isinstance(t, (FooException, UnsatisfiedRouteException)))
                .orElse(False))
# end::clazz[]
