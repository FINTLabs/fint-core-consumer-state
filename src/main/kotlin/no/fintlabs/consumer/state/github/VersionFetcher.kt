package no.fintlabs.consumer.state.github

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class VersionFetcher {

    private val webClient = WebClient.create("https://api.github.com")
    private val owner = "FINTLabs"
    private val repo = "fint-core-consumer"

    fun getReleases(): Mono<List<Release>> =
        webClient.get()
            .uri("/repos/{owner}/{repo}/releases", owner, repo)
            .retrieve()
            .bodyToFlux(Release::class.java)
            .collectList()

}