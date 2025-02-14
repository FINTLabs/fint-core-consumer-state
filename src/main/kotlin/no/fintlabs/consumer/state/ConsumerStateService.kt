package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.createId
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.fromRequest
import no.fintlabs.consumer.state.repository.ConsumerStateRepository
import no.fintlabs.webhook.server.WebhookServerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ConsumerStateService(
    private val consumerStateRepository: ConsumerStateRepository,
    private val webhookServerService: WebhookServerService
) {

    fun getConsumers(): Collection<ConsumerEntity> = consumerStateRepository.findAll()

    fun saveConsumer(consumerRequest: ConsumerRequest): Pair<ConsumerEntity, Boolean> =
        consumerStateRepository.findById(createId(consumerRequest))
            .map { it to false }
            .orElseGet {
                val entity = consumerStateRepository.save(fromRequest(consumerRequest))
                webhookServerService.callback("consumer", entity)
                entity to true
            }

    fun updateConsumer(id: String, consumerUpdate: ConsumerFields): Optional<ConsumerEntity> =
        consumerStateRepository.findById(id).map {
            consumerStateRepository.save(
                it.copy(
                    version = consumerUpdate.version ?: it.version,
                    resources = consumerUpdate.resources ?: it.resources.map { s -> s.lowercase() },
                    shared = consumerUpdate.shared ?: it.shared,
                    podResources = consumerUpdate.podResources ?: it.podResources,
                    writeableResources =
                    consumerUpdate.writeableResources ?: it.writeableResources.map { s -> s.lowercase() },
                    cacheDisabledResources =
                    consumerUpdate.cacheDisabledResources ?: it.cacheDisabledResources.map { s -> s.lowercase() },
                )
            )
        }

    fun resetAllData() = consumerStateRepository.deleteAll()

    fun deleteConsumer(id: String): Optional<ConsumerEntity> =
        consumerStateRepository.findById(id).also { it.ifPresent(consumerStateRepository::delete) }

}