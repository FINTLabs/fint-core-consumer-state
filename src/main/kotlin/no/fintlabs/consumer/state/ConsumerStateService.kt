package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.interfaces.ConsumerIdentification
import no.fintlabs.consumer.state.model.ConsumerEntity
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import java.util.*

@Service
class ConsumerStateService(
    val consumerStateRepository: ConsumerStateRepository,
) {

    fun getConsumers(): Collection<ConsumerEntity> = consumerStateRepository.findAll()

    fun saveConsumer(consumerRequest: ConsumerRequest): ConsumerIdentification {
        return consumerStateRepository.save(ConsumerEntity(consumerRequest))
    }

    fun updateConsumer(id: String, consumerUpdateRequest: ConsumerUpdateRequest): Optional<ConsumerEntity> =
        consumerStateRepository.findById(id).map {
            it.copy(
                version = consumerUpdateRequest.version ?: it.version,
                managed = consumerUpdateRequest.managed ?: it.managed,
                resources = consumerUpdateRequest.resources ?: it.resources,
                writeableResources = consumerUpdateRequest.writeableResources ?: it.writeableResources,
                cacheDisabledResources = consumerUpdateRequest.cacheDisabledResources ?: it.cacheDisabledResources,
            )
        }

    fun resetAllData() = consumerStateRepository.deleteAll()

    fun deleteConsumer(id: String): Optional<ConsumerEntity> =
        consumerStateRepository.findById(id).also { it.ifPresent(consumerStateRepository::delete) }

}