package no.fintlabs.consumer.state.validation.organization

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OrganizationFetcher(
    @Qualifier("adminWebClient") private val adminWebClient: WebClient
) {

    fun getOrganizations() =
        adminWebClient.get()
            .uri("/api/organisations")
            .retrieve()
            .bodyToFlux(Organization::class.java)

}