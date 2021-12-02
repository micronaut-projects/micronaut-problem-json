package io.micronaut.problem

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Property(name = "spec.name", value = "TaskNotFoundProblemSpec")
@MicronautTest
class TaskNotFoundProblemTest {
    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun customProblemIsRendered() {
        //given:
        val client = httpClient.toBlocking()
        //when:
        val uri = UriBuilder.of("/task").path("3").build();
        val request = HttpRequest.GET<Any>(uri)
        val e = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client.exchange(request, String::class.java)
        }
        //then:
        Assertions.assertEquals(HttpStatus.NOT_FOUND, e.status)
        Assertions.assertTrue(e.response.contentType.isPresent)
        Assertions.assertEquals("application/problem+json", e.response.contentType.get().toString())

        //when:
        val bodyOptional = e.response.getBody(Map::class.java)

        //then:
        Assertions.assertTrue(bodyOptional.isPresent)
        Assertions.assertEquals(4, bodyOptional.get().keys.size)
        Assertions.assertEquals(404, bodyOptional.get()["status"])
        Assertions.assertEquals("Not found", bodyOptional.get()["title"])
        Assertions.assertEquals("Task '3' not found", bodyOptional.get()["detail"])
        Assertions.assertEquals("https://example.org/not-found", bodyOptional.get()["type"])
    }
}