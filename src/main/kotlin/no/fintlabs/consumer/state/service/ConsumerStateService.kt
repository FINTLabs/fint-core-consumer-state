package no.fintlabs.consumer.state.service

import no.fintlabs.consumer.state.model.ConsumerState
import no.fintlabs.consumer.state.ConsumerStateRepository
import no.fintlabs.consumer.state.model.ConsumerStateUpdate
import org.springframework.stereotype.Service
import java.util.*

@Service
class ConsumerStateService(
    val consumerStateRepository: ConsumerStateRepository,
    val consumerStateValidationService: ConsumerStateValidationService
) {

    fun getConsumerStates(): Collection<ConsumerState> = consumerStateRepository.findAll()

    fun addConsumerState(consumerState: ConsumerState): ConsumerState {
//        consumerStateValidationService.validateConsumerState(consumerState)
        return consumerStateRepository.save(consumerState)
    }

    fun updateConsumerState(consumerStateUpdate: ConsumerStateUpdate, id: String): Optional<ConsumerState> =
        consumerStateRepository.findById(id).map { existingState ->
            existingState.managed = consumerStateUpdate.mutable
            consumerStateRepository.save(existingState)
        }


    fun deleteConsumerState(id: String): Optional<ConsumerState> =
        consumerStateRepository.findById(id).also {
            it.ifPresent { consumerStateRepository.deleteById(id) }
        }

}