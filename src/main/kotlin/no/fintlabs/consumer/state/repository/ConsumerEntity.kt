package no.fintlabs.consumer.state.repository

import jakarta.persistence.*
import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.PodResources

@Entity
data class ConsumerEntity(
    @Id
    val id: String,

    override val domain: String,
    override val `package`: String,
    override val org: String,
    override val version: String,
    override val shared: Boolean,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "limits.cpu", column = Column(name = "limits_cpu")),
        AttributeOverride(name = "limits.memory", column = Column(name = "limits_memory")),
        AttributeOverride(name = "requests.cpu", column = Column(name = "requests_cpu")),
        AttributeOverride(name = "requests.memory", column = Column(name = "requests_memory"))
    )
    override val podResources: PodResources,

    @ElementCollection(fetch = FetchType.EAGER)
    override val resources: List<String>,

    @ElementCollection(fetch = FetchType.EAGER)
    override val writeableResources: List<String>,

    @ElementCollection(fetch = FetchType.EAGER)
    override val cacheDisabledResources: List<String>
) : ConsumerIdentificator, ConsumerFields {
    companion object {
        fun fromRequest(consumerRequest: ConsumerRequest) = ConsumerEntity(
            createId(consumerRequest),
            consumerRequest.domain,
            consumerRequest.`package`,
            consumerRequest.org,
            consumerRequest.version,
            consumerRequest.shared,
            consumerRequest.podResources,
            consumerRequest.resources,
            consumerRequest.writeableResources,
            consumerRequest.cacheDisabledResources
        )

        fun update(consumerUpdate: ConsumerFields, existingEntity: ConsumerEntity) = existingEntity.copy(
            version = consumerUpdate.version ?: existingEntity.version,
            resources = consumerUpdate.resources ?: existingEntity.resources.map { it.lowercase() },
            shared = consumerUpdate.shared ?: existingEntity.shared,
            podResources = consumerUpdate.podResources ?: existingEntity.podResources,
            writeableResources = consumerUpdate.writeableResources
                ?: existingEntity.writeableResources.map { it.lowercase() },
            cacheDisabledResources = consumerUpdate.cacheDisabledResources
                ?: existingEntity.cacheDisabledResources.map { it.lowercase() }
        )

        fun createId(consumerRequest: ConsumerRequest) =
            "${consumerRequest.domain}-${consumerRequest.`package`}-${consumerRequest.org}"
    }
}
