package no.fintlabs.consumer.state.service

import no.fintlabs.consumer.state.ConsumerState
import no.fintlabs.consumer.state.ConsumerStateRepository
import no.fintlabs.consumer.state.ConsumerStateUpdate
import org.springframework.stereotype.Service

@Service
class ConsumerStateService(
    val consumerStateRepository: ConsumerStateRepository,
    val consumerStateValidationService: ConsumerStateValidationService
) {

    fun getConsumerStates(): Collection<ConsumerState> = consumerStateRepository.findAll()

    fun addConsumerState(consumerState: ConsumerState): ConsumerState {
        consumerStateValidationService.validateConsumerState(consumerState)
        return consumerStateRepository.save(consumerState)
    }

    fun updateConsumerState(consumerStateUpdate: ConsumerStateUpdate, id: String): Optional<ConsumerState> =
        consumerStateRepository.findById(id).map { existingState ->
            existingState.managed = consumerStateUpdate.mutable
            consumerStateRepository.save(existingState)
        }

    fun updateConsumerState(consumerState: ConsumerState, id: String) {
        consumerStateValidationService.validateConsumerState(consumerState)
        consumerStateRepository.findById(id).let {

        }
    }


}