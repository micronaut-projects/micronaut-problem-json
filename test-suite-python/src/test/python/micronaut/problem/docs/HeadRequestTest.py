from typing import Annotated

from jakarta.inject import Inject
from java.lang import String
from java.util import Map
from micronaut.context.annotation import Property
from micronaut.core.type import Argument
from micronaut.http import HttpRequest
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.client.exceptions import HttpClientResponseException
from micronaut.http.uri import UriBuilder
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test


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
