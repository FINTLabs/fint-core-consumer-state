package no.fintlabs.consumer.state.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import no.fintlabs.consumer.state.model.interfaces.ConsumerIdentification
import no.fintlabs.consumer.state.model.interfaces.ConsumerProperties

@Entity
data class ConsumerEntity(
    @Id
    val id: String,
    override val domain: String,
    override val `package`: String,
    override val org: String,
    override val version: String,
    override val managed: Boolean,
    override val resources: List<String>,
    override val writeableResources: List<String>,
    override val cacheDisabledResources: List<String>
) : ConsumerIdentification, ConsumerProperties {
    constructor(consumerRequest: ConsumerRequest) : this(
        id = "${consumerRequest.domain}-${consumerRequest.`package`}-${consumerRequest.org}",
        domain = consumerRequest.domain,
        `package` = consumerRequest.`package`,
        org = consumerRequest.org,
        version = consumerRequest.version,
        managed = consumerRequest.managed,
        resources = consumerRequest.resources,
        writeableResources = consumerRequest.writeableResources,
        cacheDisabledResources = consumerRequest.cacheDisabledResources
    )
}
