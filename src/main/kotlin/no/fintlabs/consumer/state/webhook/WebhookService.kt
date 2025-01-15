package no.fintlabs.consumer.state.webhook

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class WebhookService(
    private val webClient: WebClient = WebClient.create(),
    private val callbacks: MutableSet<String> = mutableSetOf()
) {

    fun addCallback(ip: String) = callbacks.add(ip)

    fun callBack(any: Any) = callbacks.forEach {
        webClient.post()
            .uri(it)
            .body(any, Any::class.java)
    }

}