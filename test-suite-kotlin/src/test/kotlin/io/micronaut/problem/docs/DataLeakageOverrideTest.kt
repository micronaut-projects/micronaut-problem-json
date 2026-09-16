package io.micronaut.problem.docs

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Property(name = "spec.name", value = "DataLeakageOverrideTest")
@MicronautTest
class DataLeakageOverrideTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun replacedProviderIncludesTheErrorMessageAsDetail() {
        //given:
        val client = httpClient.toBlocking()
        val okArg = Argument.of(String::class.java)
        val errorArg = Argument.of(Map::class.java)

        //when:
        val e = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.exchange(HttpRequest.GET<Any>("/foo"), okArg, errorArg)
        }

        //then:
        val bodyOptional = e.response.getBody(Map::class.java)
        Assertions.assertTrue(bodyOptional.isPresent)
        Assertions.assertEquals(3, bodyOptional.get().keys.size)
        Assertions.assertEquals(500, bodyOptional.get()["status"])
        Assertions.assertEquals("about:blank", bodyOptional.get()["type"])
        Assertions.assertEquals("Internal Server Error: foo data", bodyOptional.get()["detail"])
    }

    @Requires(property = "spec.name", value = "DataLeakageOverrideTest")
    @Controller("/foo")
    class FooController {
        @Get
        fun doSomething() {
            throw FooException("foo data")
        }
    }
}
