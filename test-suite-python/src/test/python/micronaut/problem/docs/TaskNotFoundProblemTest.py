from typing import Annotated

from jakarta.inject import Inject
from java.lang import String
from java.util import Map
from micronaut.context.annotation import Property
from micronaut.core.type import Argument
from micronaut.http import HttpRequest, HttpStatus
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.client.exceptions import HttpClientResponseException
from micronaut.http.uri import UriBuilder
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test


@Property(name="spec.name", value="TaskNotFoundProblemTest")
@MicronautTest
class TaskNotFoundProblemTest:

    http_client: Annotated[HttpClient, Inject, Client("/")]

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
