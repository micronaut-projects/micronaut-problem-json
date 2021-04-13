package io.micronaut.problem;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Status;

@Controller("/task")
public class TaskController {
    @Get("/{taskId}")
    @Status(HttpStatus.OK)
    public void index(@PathVariable Long taskId) {
        throw new TaskNotFoundProblem(taskId);
    }
}
