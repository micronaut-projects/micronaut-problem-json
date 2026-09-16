from typing import Annotated

import java
from jakarta.inject import Inject
from micronaut.context.annotation import Property
from micronaut.core.type import Argument
from micronaut.http import HttpRequest, HttpStatus
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.client.exceptions import HttpClientResponseException
from micronaut.http.uri import UriBuilder
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

# TODO(python): imported shim classes cannot be used as runtime type arguments of Argument.of / getBody
# ("TypeError: invalid instantiation of foreign object"), only java.type(...) aliases can
String = java.type("java.lang.String")
Map = java.type("java.util.Map")


@Property(name="spec.name", value="TaskNotFoundProblemTest")
@MicronautTest
class TaskNotFoundProblemTest:

    http_client: Annotated[HttpClient, Inject, Client("/")]

    # TODO(python): the Java class generated for a Python exception extending a Java exception class calls the no-arg
    # super constructor, the arguments of the Python `super().__init__(type, title, status, detail)` call are dropped, so
    # the server renders `400 {"type":"about:blank"}` instead of the 404 problem
    @Disabled("TODO(python): super-constructor arguments of a Python AbstractThrowableProblem subclass are dropped (see DISABLED_TESTS.md)")
    @Test
    def custom_problem_is_rendered(self) -> None:
        # given:
        client = self.http_client.toBlocking()
        # when:
        ok_arg = Argument.of(String)
        error_arg = Argument.of(Map)

        # then:
        try:
            client.exchange(HttpRequest.GET(UriBuilder.of("/task").path("3").build()), ok_arg, error_arg)
        except HttpClientResponseException as e:
            assert e.getStatus() == HttpStatus.NOT_FOUND
            assert e.getResponse().getContentType().isPresent()
            assert e.getResponse().getContentType().get().toString() == "application/problem+json"

            # when:
            body_optional = e.getResponse().getBody(Map)

            # then:
            assert body_optional.isPresent()
            body = body_optional.get()
            assert body.keySet().size() == 4
            assert body.get("status") == 404
            assert body.get("title") == "Not found"
            assert body.get("detail") == "Task '3' not found"
            assert body.get("type") == "https://example.org/not-found"
        else:
            assert False, "an HttpClientResponseException was expected"
