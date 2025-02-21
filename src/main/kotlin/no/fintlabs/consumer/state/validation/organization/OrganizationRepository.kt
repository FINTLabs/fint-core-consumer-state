package no.fintlabs.consumer.state.validation.organization

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository

@Repository
class OrganizationRepository(
    private val organizationFetcher: OrganizationFetcher,
    private val orgNames: MutableSet<String> = mutableSetOf()
) {

    @PostConstruct
    private fun populateOrgNames() =
        organizationFetcher.getOrganizations()
            .subscribe(
                { orgNames.add(it.name) },
                {  }
            )

    fun orgExists(org: String) = orgNames.contains(org)

}