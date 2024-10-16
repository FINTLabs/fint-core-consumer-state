package no.fintlabs.consumer.state.service

import no.fintlabs.consumer.state.model.ConsumerState
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ConsumerStateValidationService {

    fun validateConsumerState(consumerState: ConsumerState) {
        validateComponent(consumerState.domain, consumerState.`package`)
        validateVersion(consumerState.version)
        validateOrg(consumerState.org)
    }

    private fun validateComponent(domain: String, `package`: String) {
        TODO("Validate towards metamodel")
    }

    private fun validateVersion(version: String) {
        TODO("Validate towards reposolite || other places")
    }

    private fun validateOrg(org: String) {
        TODO("Figure out where we can fetch organizations")
    }

    fun throwValidationError(message: String): Nothing = throw ResponseStatusException(HttpStatus.BAD_REQUEST)

}