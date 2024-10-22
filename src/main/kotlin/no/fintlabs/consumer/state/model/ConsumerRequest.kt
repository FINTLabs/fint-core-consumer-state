package no.fintlabs.consumer.state.model

import no.fintlabs.consumer.state.model.interfaces.ConsumerIdentification
import no.fintlabs.consumer.state.model.interfaces.ConsumerProperties

data class ConsumerRequest(
    override val domain: String,
    override val `package`: String,
    override val org: String,
    override val version: String,
    override val resources: List<String> = mutableListOf(),
    override val writeableResources: List<String> = mutableListOf(),
    override val cacheDisabledResources: List<String> = mutableListOf(),
    override val managed: Boolean = true,
) : ConsumerIdentification, ConsumerProperties
