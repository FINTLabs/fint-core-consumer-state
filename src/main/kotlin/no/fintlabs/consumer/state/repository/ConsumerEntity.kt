package no.fintlabs.consumer.state.repository

import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.interfaces.ConsumerIdentificator
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.PodResources

data class ConsumerEntity(
    val id: String,
    override val domain: String,
    override val `package`: String,
    override val org: String,
    override val version: String,
    override val shared: Boolean,
    override val podResources: PodResources,
    override val resources: List<String>,
    override val writeableResources: List<String>,
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

        fun createId(consumerRequest: ConsumerRequest) =
            "${consumerRequest.domain}-${consumerRequest.`package`}-${consumerRequest.org}"
    }
}
