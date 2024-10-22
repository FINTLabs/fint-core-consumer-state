package no.fintlabs.consumer.state.model

import no.fintlabs.consumer.state.model.interfaces.ConsumerProperties

class ConsumerUpdateRequest(
    override val version: String?,
    override val managed: Boolean?,
    override val resources: List<String>?,
    override val writeableResources: List<String>?,
    override val cacheDisabledResources: List<String>?
): ConsumerProperties {
}