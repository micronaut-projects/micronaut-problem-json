package io.micronaut.problem.docs;

public class FooException extends RuntimeException {
    FooException(String message) {
        super(message);
    }
}
