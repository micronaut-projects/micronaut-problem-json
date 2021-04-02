package io.micronaut.problem

class ProblemDisabledSpec extends ApplicationContextSpecification {

    @Override
    Map<String, Object> getConfiguration() {
        super.configuration + [
                'problem.enabled': false
        ]
    }

    void 'if you disable problem with problem.enabled false Problem related beans are not loaded'() {
        expect:
        !applicationContext.containsBean(ProblemErrorResponseProcessor)
    }
}
