package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.zalando.problem.DefaultProblem

import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ConstraintViolationProblemSpec extends EmbeddedServerSpecification {

    @Override
    @Nullable
    String getSpecName() {
        'ConstraintViolationProblemSpec'
    }

    void "ConstraintViolationException is rendered as a problem"() {
        when:
        Argument<?> okArg = Argument.of(String)
        Argument<?> errorArg = Argument.of(Map)
        client.exchange(HttpRequest.POST('/contact', new ContactCreateForm()), okArg, errorArg)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST
        e.response.contentType.isPresent()
        e.response.contentType.get().toString() == 'application/problem+json'

        when:
        Optional<Map> bodyOptional = e.response.getBody(errorArg)

        then:
        bodyOptional.isPresent()
        bodyOptional.get().keySet().size() == 4
        bodyOptional.get()['status'] == 400
        bodyOptional.get()['title'] == 'Constraint Violation'
        bodyOptional.get()['type'] == "https://zalando.github.io/problem/constraint-violation"
        bodyOptional.get()['violations']
        bodyOptional.get()['violations'][0]['field'] == 'save.contact.name'
        bodyOptional.get()['violations'][0]['message'] == 'must not be blank'
    }

    @Requires(property = 'spec.name', value = 'ConstraintViolationProblemSpec')
    @Controller('/contact')
    static class ContactController {

        @Post
        @Status(HttpStatus.CREATED)
        void save(@Body @NonNull @NotNull @Valid ContactCreateForm contact) {

        }
    }

    @Introspected
    static class ContactCreateForm {
        @NonNull
        @NotBlank
        String name
    }
}
