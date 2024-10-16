package no.fintlabs.consumer.state.service

import no.fintlabs.consumer.state.ConsumerState
import org.springframework.stereotype.Service

@Service
class ConsumerStateValidationService {

    fun validateConsumerState(consumerState: ConsumerState) {
        validateComponent(consumerState.domain, consumerState.`package`)
        validateVersion(consumerState.version)
        validateOrg(consumerState.org)
    }

    private fun validateOrg(org: String) {
        TODO("Figure out where we can fetch organizations")
    }

    private fun validateVersion(version: String) {
        TODO("Validate towards reposolite || other places")
    }

    private fun validateComponent(domain: String, `package`: String) {
        TODO("Validate towards metamodel")
    }

    fun throwValidationError(message: String): Nothing = throw ConsumerStateValidationException(message)

}