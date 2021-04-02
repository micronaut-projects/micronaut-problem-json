package io.micronaut.problem;

import io.micronaut.http.HttpStatus;
import org.zalando.problem.AbstractThrowableProblem;

import java.net.URI;

public class TaskNotFoundProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create("https://example.org/not-found");

    public TaskNotFoundProblem(Long taskId) {
        super(TYPE, "Not found", new HttpStatusType(HttpStatus.NOT_FOUND), String.format("Task '%s' not found", taskId));
    }
}
