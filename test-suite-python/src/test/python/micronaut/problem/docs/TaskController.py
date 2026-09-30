from typing import Annotated

from micronaut.http import HttpStatus
from micronaut.http.annotation import Controller, Get, PathVariable, Status

from .TaskNotFoundProblem import TaskNotFoundProblem


@Controller("/task")
class TaskController:

    @Get("/{taskId}")
    @Status(HttpStatus.OK)
    def index(self, taskId: Annotated[int, PathVariable]) -> None:
        raise TaskNotFoundProblem(taskId)
