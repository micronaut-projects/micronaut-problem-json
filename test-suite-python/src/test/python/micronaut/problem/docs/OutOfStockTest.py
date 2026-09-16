from typing import Annotated

import java
from jakarta.inject import Inject
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


@MicronautTest
class OutOfStockTest:

    http_client: Annotated[HttpClient, Inject, Client("/")]

    # TODO(python): the keyword-safe alias `with_` of `ProblemBuilder.with(String, Object)` is not available on the foreign
    # builder returned by `Problem.builder()` ("AttributeError: foreign object has no attribute 'with_'")
    @Disabled("TODO(python): keyword-safe alias with_() is not available on the foreign ProblemBuilder (see DISABLED_TESTS.md)")
    @Test
    def custom_problem_is_rendered(self) -> None:
        # given:
        client = self.http_client.toBlocking()
        # when:
        ok_arg = Argument.of(String)
        error_arg = Argument.of(Map)
        try:
            client.exchange(HttpRequest.GET(UriBuilder.of("/product").build()), ok_arg, error_arg)
        except HttpClientResponseException as e:
            # then:
            assert e.getStatus() == HttpStatus.BAD_REQUEST
            assert e.getResponse().getContentType().isPresent()
            assert e.getResponse().getContentType().get().toString() == "application/problem+json"

            # when:
            body_optional = e.getResponse().getBody(Map)

            # then:
            assert body_optional.isPresent()
            body = body_optional.get()
            assert body.keySet().size() == 5
            assert body.get("status") == 400
            assert body.get("title") == "Out of Stock"
            assert body.get("detail") == "Item B00027Y5QG is no longer available"
            assert body.get("type") == "https://example.org/out-of-stock"
            assert body.get("parameters") == {"product": "B00027Y5QG"}
        else:
            assert False, "an HttpClientResponseException was expected"
