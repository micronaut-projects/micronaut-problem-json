from java.net import URI
from micronaut.http import HttpStatus
from micronaut.http.annotation import Controller, Get, Status
from micronaut.problem import HttpStatusType
from org.zalando.problem import Problem


@Controller("/product")
class ProductController:

    @Get
    @Status(HttpStatus.OK)
    def index(self) -> None:
        raise (Problem.builder()
               .withType(URI.create("https://example.org/out-of-stock"))
               .withTitle("Out of Stock")
               .withStatus(HttpStatusType(HttpStatus.BAD_REQUEST))
               .withDetail("Item B00027Y5QG is no longer available")
               .with_("product", "B00027Y5QG")
               .build())
