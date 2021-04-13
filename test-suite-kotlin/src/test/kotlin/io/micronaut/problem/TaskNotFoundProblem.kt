package io.micronaut.problem

import com.fasterxml.jackson.annotation.JsonIgnore
import io.micronaut.http.HttpStatus
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import java.net.URI

class TaskNotFoundProblem(taskId: Long) :
        AbstractThrowableProblem(URI.create("https://example.org/not-found"),
                "Not found",
                HttpStatusType(HttpStatus.NOT_FOUND),
                String.format("Task '%s' not found", taskId)) {

    @JsonIgnore
    override fun getCause(): Exceptional {
        TODO("Not yet implemented")
    }
}