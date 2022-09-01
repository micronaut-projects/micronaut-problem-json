package io.micronaut.problem;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.http.HttpStatus;
import io.micronaut.serde.annotation.Serdeable;
import org.zalando.problem.AbstractThrowableProblem;

@Serdeable
public class ProductProblem extends AbstractThrowableProblem {

    private final String field;

    public ProductProblem(String field) {
        super(null, HttpStatus.INTERNAL_SERVER_ERROR.getReason(), new HttpStatusType(HttpStatus.INTERNAL_SERVER_ERROR));
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
