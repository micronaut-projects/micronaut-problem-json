package io.micronaut.problem;

public class FooException extends RuntimeException {
    FooException(String message) {
        super(message);
    }
}
