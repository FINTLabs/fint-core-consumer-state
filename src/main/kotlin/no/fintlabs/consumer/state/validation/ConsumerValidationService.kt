package no.fintlabs.consumer.state.validation

import no.fintlabs.consumer.state.interfaces.ConsumerFields
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity.Companion.createId
import no.fintlabs.consumer.state.validation.github.VersionRepository
import no.fintlabs.consumer.state.validation.metadata.MetadataRepository
import no.fintlabs.consumer.state.validation.organization.OrganizationRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ConsumerValidationService(
    private val organizationRepository: OrganizationRepository,
    private val metadataRepository: MetadataRepository,
    private val versionRepository: VersionRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun validateRequest(consumerRequest: ConsumerRequest) {
        val validOrg = organizationRepository.orgExists(consumerRequest.org)
        val validComponent = metadataRepository.containsComponent(consumerRequest.domain, consumerRequest.`package`)

        logger.info("VALID ORG: $validOrg value: ${consumerRequest.org}")
        if (validOrg && validComponent) validateConsumerFields(createId(consumerRequest), consumerRequest)
        else throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Invalid organization or component"
        )
    }

    fun validateConsumerFields(id: String, consumer: ConsumerFields) {
        val (domain, `package`, _) = id.split("-")
        val validVersion = consumer.version?.let { versionRepository.versionExists(it) } ?: false

        if (!validVersion) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid version")
        }

        val validResources = validateResources(domain, `package`, consumer)

        logger.info("Valid resources: $validResources - $domain $`package`")
        if (!validResources) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid resources")
        }
    }

    fun validateResources(domain: String, pkg: String, consumer: ConsumerFields) =
        listOfNotNull(
            consumer.resources,
            consumer.writeableResources,
            consumer.cacheDisabledResources
        ).flatten()
            .all { metadataRepository.resourceBelongsToComponent(domain, pkg, it) }

}
