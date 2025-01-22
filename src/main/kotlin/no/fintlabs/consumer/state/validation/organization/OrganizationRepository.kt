package no.fintlabs.consumer.state.validation.organization

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class OrganizationRepository(
    private val organizationFetcher: OrganizationFetcher
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val orgNames: MutableSet<String> = mutableSetOf()

    @PostConstruct
    private fun populateOrgNames() = organizationFetcher.getOrganizations().subscribe{ orgNames.add(it.name) }

    fun orgExists(org: String) = orgNames.contains(org)

}