from java.net import URI
from micronaut.http import HttpStatus
from micronaut.problem import HttpStatusType
from org.zalando.problem import AbstractThrowableProblem

TYPE = URI.create("https://example.org/not-found")


class TaskNotFoundProblem(AbstractThrowableProblem):

    def __init__(self, task_id: int):
        super().__init__(TYPE, "Not found", HttpStatusType(HttpStatus.NOT_FOUND), f"Task '{task_id}' not found")
