package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerResponse
import no.fintlabs.consumer.state.model.Operation
import no.fintlabs.consumer.state.repository.ConsumerEntity
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.createId
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.fromRequest
import no.fintlabs.consumer.state.repository.ConsumerStateRepository
import no.fintlabs.webhook.server.WebHookServerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ConsumerStateService(
    private val consumerStateRepository: ConsumerStateRepository,
    private val webHookServerService: WebHookServerService
) {

    fun getConsumers(): Collection<ConsumerEntity> = consumerStateRepository.cache.values

    fun saveConsumer(consumerRequest: ConsumerRequest): ConsumerEntity {
        val fromRequest = fromRequest(consumerRequest)
        consumerStateRepository.cache[createId(consumerRequest)] = fromRequest
        webHookServerService.callback(ConsumerResponse(
            consumerRequest,
            Operation.CREATE
        ))
        return fromRequest
    }

    fun resetAllData() = consumerStateRepository.cache.clear()

}