# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Problem JSON that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps). Use it as the
bug-fixing task list for the final migration wave.

## Reconciliation

- Last generated active `@Disabled` count: 1.
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 4 tests executed (4 test classes), 1 skipped (core 5.2.3 / micronaut-build 8.1.2).

## Migration Rules

- Python sources must not live in a package whose `__init__.py` the Python compiler also generates for an imported
  Java package: `micronaut/problem/*.py` would collide with the shims of `io.micronaut.problem` (`HttpStatusType`), so the
  snippet classes were moved to `io.micronaut.problem.docs` in every language.
- `Argument.of(String)`, `Argument.of(Map)` and `getBody(Map)` use the imported `java.lang.String` / `java.util.Map` classes.
- Java exceptions are raised with `raise` (a `ThrowableProblem` built by `Problem.builder()`, or a Python subclass of
  `AbstractThrowableProblem`) and caught in tests with `except HttpClientResponseException as e`.
- The path variable of `TaskController.index` keeps the Java name `taskId` (argument-name-bound); a Java `Long` is
  a Python `int`.

## Active `@Disabled` Tests

| Test | Reason |
| --- | --- |
| `OutOfStockTest.custom_problem_is_rendered` | The keyword-safe alias `with_` works with core 5.2.3, but the Java `DefaultProblem` that `ProductController` builds and raises from Python arrives in the server with a Truffle `TruffleStackTrace.LazyStackTrace` in its suppressed exceptions (Truffle attaches it to a host exception that unwinds through Python frames), and the problem+json body provider fails to serialize it: `CodecException: Error encoding object [ProblemJsonErrorResponseBodyProvider$ThrowableProblemWithoutStacktrace] to JSON: No serializable introspection present for type LazyStackTrace` (`CustomizedObjectArraySerializer` of the `suppressed` array), so the client gets a 500 instead of the 400 problem. A Python exception class extending a Java exception (`TaskNotFoundProblem`) is not affected. |

## Commented Unsupported Snippet Ports

None.

## Intentionally Unsupported Snippet Targets

None (`ProblemErrorResponseProcessorReplacement` extends `ProblemJsonErrorResponseBodyProvider` directly with core 5.2.3 and
overrides the protected `includeErrorMessage` hook; `DataLeakageOverrideTest` exercises it).
