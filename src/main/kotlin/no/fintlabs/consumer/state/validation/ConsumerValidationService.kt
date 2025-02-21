package no.fintlabs.consumer.state.validation

import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.createId
import no.fintlabs.consumer.state.validation.github.VersionRepository
import no.fintlabs.consumer.state.validation.metadata.MetadataRepository
import no.fintlabs.consumer.state.validation.organization.OrganizationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ConsumerValidationService(
    private val organizationRepository: OrganizationRepository,
    private val metadataRepository: MetadataRepository,
    private val versionRepository: VersionRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun validateRequest(consumerRequest: ConsumerRequest): Boolean {
        val validOrg = validateOrgName(consumerRequest.org)
        val validComponent = metadataRepository.containsComponent(consumerRequest.domain, consumerRequest.`package`)

        return if (organisationAndComponentIsValid(validOrg, validComponent))
            validateConsumerFields(createId(consumerRequest), consumerRequest)
        else false
    }

    fun organisationAndComponentIsValid(validOrg: Boolean, validComponent: Boolean) =
        (validOrg && validComponent).also { logger.debug("validOrg: $validOrg validComponent: $validComponent") }

    fun validateConsumerFields(id: String, consumer: ConsumerFields): Boolean {
        val (domain, `package`, _) = id.split("-")
        val validVersion = consumer.version?.let { versionRepository.versionExists(it) } ?: false

        if (!validVersion) {
            logger.debug("Invalid version")
            return false
        }

        val validResources = validateResources(domain, `package`, consumer)

        if (!validResources) {
            logger.debug("Invalid resources")
            return false
        }

        return true
    }

    fun validateOrgName(orgName: String): Boolean =
        organizationRepository.isEmpty().takeIf { it }
            ?.also { println("Organization is empty, therefore disabled org validation") }
            ?: organizationRepository.orgExists(orgName)

    fun validateResources(domain: String, pkg: String, consumer: ConsumerFields) =
        listOfNotNull(
            consumer.resources,
            consumer.writeableResources,
            consumer.cacheDisabledResources
        ).flatten()
            .all { metadataRepository.resourceBelongsToComponent(domain, pkg, it) }

}
