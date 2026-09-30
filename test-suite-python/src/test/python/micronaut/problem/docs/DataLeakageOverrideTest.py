from typing import Annotated

from jakarta.inject import Inject
from java.lang import String
from java.util import Map
from micronaut.context.annotation import Property, Requires
from micronaut.core.type import Argument
from micronaut.http import HttpRequest
from micronaut.http.annotation import Controller, Get
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.client.exceptions import HttpClientResponseException
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .FooException import FooException


@Requires(property="spec.name", value="DataLeakageOverrideTest")
@Controller("/foo")
class FooController:

    @Get
    def do_something(self) -> None:
        raise FooException("foo data")


@Property(name="spec.name", value="DataLeakageOverrideTest")
@MicronautTest
class DataLeakageOverrideTest:

    http_client: Annotated[HttpClient, Inject, Client("/")]

    @Test
    def replaced_provider_includes_the_error_message_as_detail(self) -> None:
        # given:
        client = self.http_client.toBlocking()
        ok_arg = Argument.of(String)
        error_arg = Argument.of(Map)

        # when:
        try:
            client.exchange(HttpRequest.GET("/foo"), ok_arg, error_arg)
        except HttpClientResponseException as e:
            # then:
            body_optional = e.getResponse().getBody(Map)
            assert body_optional.isPresent()
            body = body_optional.get()
            assert body.keySet().size() == 3
            assert body.get("status") == 500
            assert body.get("type") == "about:blank"
            assert body.get("detail") == "Internal Server Error: foo data"
        else:
            assert False, "an HttpClientResponseException was expected"
