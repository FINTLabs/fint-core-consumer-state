package no.fintlabs.consumer.state.repository

import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("consumer_entity")
data class ConsumerEntity(
    @Id
    val id: String,

    override val domain: String,
    override val `package`: String,
    override val org: String,
    override val version: String,
    override val shared: Boolean,
    override val limitsCpu: String,
    override val limitsMemory: String,
    override val requestsCpu: String,
    override val requestsMemory: String,
    override val resources: List<String>,
    override val writeableResources: List<String>,
    override val cacheDisabledResources: List<String>
) : Consumer {
    companion object {
        fun fromRequest(consumerRequest: ConsumerRequest) = ConsumerEntity(
            createId(consumerRequest),
            consumerRequest.domain,
            consumerRequest.`package`,
            consumerRequest.org,
            consumerRequest.version,
            consumerRequest.shared,
            consumerRequest.limitsCpu,
            consumerRequest.limitsMemory,
            consumerRequest.requestsCpu,
            consumerRequest.requestsMemory,
            consumerRequest.resources,
            consumerRequest.writeableResources,
            consumerRequest.cacheDisabledResources
        )

        fun update(consumerUpdate: ConsumerFields, existingEntity: ConsumerEntity) = existingEntity.copy(
            version = consumerUpdate.version ?: existingEntity.version,
            shared = consumerUpdate.shared ?: existingEntity.shared,
            limitsCpu = consumerUpdate.limitsCpu ?: existingEntity.limitsCpu,
            limitsMemory = consumerUpdate.limitsMemory ?: existingEntity.limitsMemory,
            requestsCpu = consumerUpdate.requestsCpu ?: existingEntity.requestsCpu,
            requestsMemory = consumerUpdate.requestsMemory ?: existingEntity.requestsMemory,
            resources = consumerUpdate.resources ?: existingEntity.resources.map { it.lowercase() },
            writeableResources = consumerUpdate.writeableResources
                ?: existingEntity.writeableResources.map { it.lowercase() },
            cacheDisabledResources = consumerUpdate.cacheDisabledResources
                ?: existingEntity.cacheDisabledResources.map { it.lowercase() }
        )

        fun createId(consumerRequest: ConsumerRequest) =
            "${consumerRequest.domain}-${consumerRequest.`package`}-${consumerRequest.org}"
    }
}
