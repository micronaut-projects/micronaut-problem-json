package io.micronaut.problem;

import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;

@Serdeable
public class Widget {
    @NotBlank
    String item;

    public String getItem() {
        return item;
    }
}
