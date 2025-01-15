package no.fintlabs.consumer.state.webhook

import org.springframework.stereotype.Service

@Service
class WebhookService {

    private val callbacks: MutableSet<String> = mutableSetOf()

    fun addCallback(ip: String) = callbacks.add(ip)

    fun getCallbacks() = callbacks.toSet()

}