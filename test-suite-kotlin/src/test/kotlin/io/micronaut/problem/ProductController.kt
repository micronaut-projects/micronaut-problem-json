package io.micronaut.problem

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import org.zalando.problem.Problem
import java.net.URI

@Controller("/product")
class ProductController {
    @Get
    @Status(HttpStatus.OK)
    fun index() {
        throw Problem.builder()
                .withType(URI.create("https://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(HttpStatusType(HttpStatus.BAD_REQUEST))
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build()
    }
}