package no.fintlabs.consumer.state.webhook

import no.fintlabs.consumer.state.model.ConsumerEntity
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.Operation
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class WebhookService(
    private val webClient: WebClient = WebClient.create(),
    private val callbacks: MutableSet<String> = mutableSetOf()
) {

    fun addCallback(ip: String) = callbacks.add(ip)

    fun callBack(consumerEntity: ConsumerEntity, operation: Operation) = callbacks.forEach {
        webClient.post()
            .uri(it)
            .body(ConsumerRequest.fromEntity(consumerEntity, operation), ConsumerRequest::class.java)
    }

}