package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerEntity
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import no.fintlabs.consumer.state.model.interfaces.ConsumerIdentification
import no.fintlabs.consumer.state.model.validation.ConsumerValidator
import no.fintlabs.consumer.state.webhook.WebhookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/consumer")
class ConsumerStateController(
    private val consumerStateService: ConsumerStateService,
    private val consumerValidator: ConsumerValidator,
    private val webhookService: WebhookService
) {

    @GetMapping
    fun getConsumers(): Collection<ConsumerEntity> = consumerStateService.getConsumers()

    @PostMapping
    fun addConsumer(
        @RequestBody consumerRequest: ConsumerRequest,
        webExchange: ServerWebExchange
    ): ResponseEntity<ConsumerEntity> {
        consumerValidator.validateRequest(consumerRequest)
        return consumerStateService.saveConsumer(consumerRequest).let {
            webhookService.callBack(it)
            ResponseEntity.created(URI.create("${webExchange.request.uri}${it.domain}")).body(it)
        }
    }

    @PutMapping("/{id}")
    fun updateConsumer(
        @PathVariable id: String,
        @RequestBody consumerUpdateRequest: ConsumerUpdateRequest
    ): ResponseEntity<ConsumerEntity> {
        consumerValidator.validateUpdateRequest(id, consumerUpdateRequest)
        return consumerStateService.updateConsumer(id, consumerUpdateRequest).map {
            webhookService.callBack(it)
            ResponseEntity.ok(it)
        }.orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Consumer id not found: $id")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteConsumer(@PathVariable id: String): ResponseEntity<Void> =
        consumerStateService.deleteConsumer(id).map {
            webhookService.callBack(it)
            ResponseEntity.noContent().build<Void>()
        }.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    // TODO: Temporary, get rid of this in production
    @PatchMapping
    fun resetAllData() = consumerStateService.resetAllData()

}