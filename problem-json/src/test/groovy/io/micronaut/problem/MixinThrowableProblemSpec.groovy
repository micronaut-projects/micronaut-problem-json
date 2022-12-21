package io.micronaut.problem

import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.serde.annotation.Serdeable

class MixinThrowableProblemSpec extends EmbeddedServerSpecification {

    @Override
    String getSpecName() {
        'MixinThrowableProblemSpec'
    }

    void 'MixinThrowableProblem is only applied to ThrowableProblem'() {
        when:
        List<Map> l = client.retrieve(HttpRequest.GET('/messages'), Argument.listOf(Map))

        then:
        l
        l[0].containsKey('message')
        l[0]['message'] == 'Hello World'
    }

    @Requires(property = 'spec.name', value = 'MixinThrowableProblemSpec')
    @Controller("/messages")
    static class MessagesController {

        @Get
        List<Message> index() {
            [new Message(message: 'Hello World')]
        }
    }

    @Serdeable
    static class Message {
        String message
    }
}
