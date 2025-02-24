package no.fintlabs.consumer.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerResponse
import no.fintlabs.consumer.state.model.Operation
import no.fintlabs.consumer.state.repository.ConsumerEntity
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.fromRequest
import no.fintlabs.consumer.state.repository.ConsumerStateRepository
import no.fintlabs.webhook.server.WebhookServerService
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service

@Service
class ConsumerStateService(
    private val consumerStateRepository: ConsumerStateRepository,
    private val webhookServerService: WebhookServerService,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) {

    fun getConsumers(): Flow<ConsumerEntity> = consumerStateRepository.findAll().asFlow()

    suspend fun saveConsumer(consumerRequest: ConsumerRequest): Pair<ConsumerEntity, Boolean> {
        val id = ConsumerEntity.createId(consumerRequest)
        return consumerStateRepository.findById(id)
            .awaitSingleOrNull()?.let {
                it to false
            } ?: run {
            val entity = r2dbcEntityTemplate.insert(fromRequest(consumerRequest)).awaitSingle()
            webhookCallback(entity, Operation.CREATE)
            entity to true
        }
    }

    suspend fun updateConsumer(id: String, consumerUpdate: ConsumerFields): ConsumerEntity? =
        consumerStateRepository.findById(id).awaitSingleOrNull()?.let { existing ->
            val newConsumer = consumerStateRepository.save(ConsumerEntity.update(consumerUpdate, existing))
                .awaitSingle()
            webhookCallback(newConsumer, Operation.UPDATE)
            newConsumer
        }

    suspend fun deleteConsumer(id: String): ConsumerEntity? =
        consumerStateRepository.findById(id).awaitSingleOrNull()?.let {
            consumerStateRepository.delete(it).subscribe()
            webhookCallback(it, Operation.DELETE)
            it
        }

    private fun webhookCallback(consumerEntity: ConsumerEntity, operation: Operation) =
        webhookServerService.callback(
            "consumer",
            ConsumerResponse(ConsumerRequest.fromConsumer(consumerEntity), operation)
        )

    suspend fun getConsumer(id: String): ConsumerEntity? =
        consumerStateRepository.findById(id).awaitSingleOrNull()

}
