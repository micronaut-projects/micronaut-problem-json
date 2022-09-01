package io.micronaut.problem;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import org.zalando.problem.Problem;

import javax.validation.Valid;
import java.net.URI;

@Controller("/product")
public class ProductController {
    @Get
    @Status(HttpStatus.OK)
    public void index() {
        throw Problem.builder()
                .withType(URI.create("https://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(new HttpStatusType(HttpStatus.BAD_REQUEST))
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build();
    }

    @Get("/problem")
    public void problem() {
        throw new ProductProblem("random");
    }

    @Post
    public HttpResponse<String> add(@Valid @Body Widget item) {
        return HttpResponse.ok(item.getItem());
    }
}
