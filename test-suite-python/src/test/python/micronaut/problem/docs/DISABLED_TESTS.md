# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Problem JSON that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps). Use it as the
bug-fixing task list for the final migration wave.

## Reconciliation

- Last generated active `@Disabled` count: 2.
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 3 tests executed (3 test classes), 2 skipped.

## Migration Rules

- Python sources must not live in a package whose `__init__.py` the Python compiler also generates for an imported
  Java package: `micronaut/problem/*.py` would collide with the shims of `io.micronaut.problem` (`HttpStatusType`), so the
  snippet classes were moved to `io.micronaut.problem.docs` in every language.
- Imported shim classes of Java types cannot be used as runtime type arguments (`TypeError: invalid instantiation of
  foreign object`): `Argument.of(String)`, `Argument.of(Map)` and `getBody(Map)` use `java.type(...)` aliases
  (marked `# TODO(python)`).
- Java exceptions are raised with `raise` (a `ThrowableProblem` built by `Problem.builder()`, or a Python subclass of
  `AbstractThrowableProblem`) and caught in tests with `except HttpClientResponseException as e`.
- The path variable of `TaskController.index` keeps the Java name `taskId` (argument-name-bound); a Java `Long` is
  a Python `int`.

## Active `@Disabled` Tests

| Test | Reason |
| --- | --- |
| `TaskNotFoundProblemTest.custom_problem_is_rendered` | The Java class generated for `TaskNotFoundProblem(AbstractThrowableProblem)` calls the no-arg `super()` in its constructors, so the arguments of the Python `super().__init__(TYPE, "Not found", HttpStatusType(HttpStatus.NOT_FOUND), detail)` call never reach the Java side: the server renders `400 {"type":"about:blank"}` instead of the 404 problem (same family as the dropped message of a Python `RuntimeException` subclass). |
| `OutOfStockTest.custom_problem_is_rendered` | `ProductController` calls `ProblemBuilder.with(String, Object)` through its keyword-safe alias `with_`, which the runtime does not expose on the foreign builder returned by `Problem.builder()`: `AttributeError: foreign object has no attribute 'with_'`. |

## Commented Unsupported Snippet Ports

None.

## Intentionally Unsupported Snippet Targets

| Target | Reason |
| --- | --- |
| `io.micronaut.problem.docs.ProblemErrorResponseProcessorReplacement` (`languages="java,kotlin,groovy"`) | Extends the Java class `ProblemJsonErrorResponseBodyProvider` to override its protected `includeErrorMessage`; the Python runtime rejects it at import time with `RuntimeError: Native Python mode does not support Python class [ProblemErrorResponseProcessorReplacement] extending Java class [io.micronaut.problem.ProblemJsonErrorResponseBodyProvider]; use composition or a Java interface instead`, and composition cannot override a protected hook. The guide carries a `[.lang-python]` note. |
