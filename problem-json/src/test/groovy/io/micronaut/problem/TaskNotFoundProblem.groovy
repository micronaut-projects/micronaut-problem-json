package io.micronaut.problem

import io.micronaut.http.HttpStatus
import io.micronaut.serde.annotation.Serdeable
import org.zalando.problem.AbstractThrowableProblem

@Serdeable
class TaskNotFoundProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("https://example.org/not-found")

    TaskNotFoundProblem(Long taskId) {
        super(TYPE, "Not found", new HttpStatusType(HttpStatus.NOT_FOUND), String.format("Task '%s' not found", taskId))
    }
}
