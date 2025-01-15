package no.fintlabs.consumer.state.webhook

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/webhook")
class WebhookController(
    private val webhookService: WebhookService
) {

    @PostMapping
    fun register(registerRequest: RegisterRequest) = webhookService.addCallback(registerRequest.callback)

}