package no.fintlabs.consumer.state.model.validation

import no.fintlabs.consumer.state.exception.InvalidConsumerException
import no.fintlabs.consumer.state.github.VersionRepository
import no.fintlabs.consumer.state.metadata.MetadataRepository
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import no.fintlabs.consumer.state.model.interfaces.ConsumerProperties
import org.springframework.stereotype.Service

@Service
class ConsumerValidator(
    val metadataRepository: MetadataRepository,
    val versionRepository: VersionRepository
) {

    fun validateRequest(consumerRequest: ConsumerRequest) =
        validateConsumer(consumerRequest.domain, consumerRequest.`package`, consumerRequest.org, consumerRequest)

    fun validateUpdateRequest(id: String, consumerUpdateRequest: ConsumerUpdateRequest) =
        id.split("-").let { validateConsumer(it[0], it[1], it[2], consumerUpdateRequest) }

    private fun validateConsumer(domain: String, `package`: String, org: String, consumerProperties: ConsumerProperties) {
        validateComponent(domain, `package`)
        validateOrganization(org)

        consumerProperties.version?.let { validateVersion(it) }
        consumerProperties.resources?.let { validateResources(domain, `package`, "resources", it) }
        consumerProperties.writeableResources?.let { validateResources(domain, `package`, "writeableResources", it) }
        consumerProperties.cacheDisabledResources?.let { validateResources(domain, `package`, "cacheDisabledResources", it) }
    }

    private fun validateComponent(domain: String, `package`: String) {
        if (!metadataRepository.containsComponent(domain, `package`))
           throw InvalidConsumerException("Component: '$domain-$`package`' does not exist")
    }

    private fun validateResources(domain: String, `package`: String, fieldName: String, resources: List<String>) {
        resources.forEach { resource ->
            if (!metadataRepository.resourceBelongsToComponent(domain, `package`, resource))
                throw InvalidConsumerException("Resource: $resource in field: $fieldName does not exist in '$domain-$`package`' component")
        }
    }

    // TODO: Implement org validation
    private fun validateOrganization(org: String) = true

    private fun validateVersion(version: String) = versionRepository.versionExists(version)

}