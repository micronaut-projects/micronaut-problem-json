package io.micronaut.problem

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Status

@Controller("/task")
class TaskController {
    @Get("/{taskId}")
    @Status(HttpStatus.OK)
    fun index(@PathVariable taskId: Long) {
        throw TaskNotFoundProblem(taskId)
    }
}