package no.fintlabs.consumer.state.model.validation

class ConsumerValidationException(val errors: MutableMap<String, Any>) : Throwable() {
}
