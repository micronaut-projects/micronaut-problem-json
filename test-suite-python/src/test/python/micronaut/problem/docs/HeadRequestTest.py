from typing import Annotated

import java
from jakarta.inject import Inject
from micronaut.context.annotation import Property
from micronaut.core.type import Argument
from micronaut.http import HttpRequest
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.client.exceptions import HttpClientResponseException
from micronaut.http.uri import UriBuilder
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

# TODO(python): imported shim classes cannot be used as runtime type arguments of Argument.of
# ("TypeError: invalid instantiation of foreign object"), only java.type(...) aliases can
String = java.type("java.lang.String")
Map = java.type("java.util.Map")


@Property(name="spec.name", value="HeadRequestTest")
@Property(name="micronaut.http.client.read-timeout", value="10m")
@MicronautTest
class HeadRequestTest:

    http_client: Annotated[HttpClient, Inject, Client("/")]

    @Test
    def head_request_has_no_body_nor_content_type(self) -> None:
        # given:
        client = self.http_client.toBlocking()

        # when:
        ok_arg = Argument.of(String)
        error_arg = Argument.of(Map)

        # then:
        try:
            client.exchange(HttpRequest.HEAD(UriBuilder.of("/task").path("3").build()), ok_arg, error_arg)
        except HttpClientResponseException as e:
            assert not e.getResponse().getContentType().isPresent()
            assert not e.getResponse().getBody().isPresent()
        else:
            assert False, "an HttpClientResponseException was expected"
