package no.fintlabs.consumer.state.service

import no.fintlabs.consumer.state.ConsumerState
import no.fintlabs.consumer.state.ConsumerStateRepository
import org.springframework.stereotype.Service

@Service
class ConsumerStateService(
    val consumerStateRepository: ConsumerStateRepository,
    val consumerStateValidationService: ConsumerStateValidationService
) {

    fun getConsumerStates(): Collection<ConsumerState> = consumerStateRepository.findAll()

    fun addConsumerState(consumerState: ConsumerState) {
        consumerStateValidationService.validateConsumerState(consumerState)
        consumerStateRepository.save(consumerState)
    }

    fun updateConsumerState(consumerState: ConsumerState, id: String) {
        consumerStateValidationService.validateConsumerState(consumerState)
        consumerStateRepository.findById(id).let {

        }
    }


}