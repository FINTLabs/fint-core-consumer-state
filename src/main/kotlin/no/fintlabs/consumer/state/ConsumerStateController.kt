package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerEntity
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import no.fintlabs.consumer.state.model.interfaces.ConsumerIdentification
import no.fintlabs.consumer.state.model.validation.ConsumerValidator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/consumer-state")
class ConsumerStateController(
    val consumerStateService: ConsumerStateService,
    val consumerValidator: ConsumerValidator
) {

    @GetMapping
    fun getConsumers(): Collection<ConsumerEntity> = consumerStateService.getConsumers()

    @PostMapping
    fun addConsumer(
        @RequestBody consumerRequest: ConsumerRequest,
        webExchange: ServerWebExchange
    ): ResponseEntity<ConsumerIdentification> {
        consumerValidator.validateRequest(consumerRequest)
        return consumerStateService.saveConsumer(consumerRequest).let {
            ResponseEntity.created(URI.create("${webExchange.request.uri}${it.domain}")).body(it)
        }
    }

    @PutMapping("/{id}")
    fun updateConsumer(
        @PathVariable id: String,
        @RequestBody consumerUpdateRequest: ConsumerUpdateRequest,
    ): ConsumerEntity {
        consumerValidator.validateUpdateRequest(id, consumerUpdateRequest)
        return consumerStateService.updateConsumer(id, consumerUpdateRequest)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    }

    @DeleteMapping("/{id}")
    fun deleteConsumer(@PathVariable id: String): ConsumerEntity =
        consumerStateService.deleteConsumer(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    // TODO: Temporary, get rid of this in production
    @PatchMapping
    fun resetAllData() = consumerStateService.resetAllData()

}