package no.fintlabs.consumer.state.metadata

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class MetadataFetcher(
    @Qualifier("metamodelWebClient") val webClient: WebClient
) {

    fun getMetadata() =
        webClient.get()
            .retrieve()
            .bodyToFlux(Metadata::class.java)

}