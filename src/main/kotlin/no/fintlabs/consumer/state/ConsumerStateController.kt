package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/consumer")
class ConsumerStateController(private val consumerStateService: ConsumerStateService) {

    @GetMapping
    fun getConsumers(): Collection<ConsumerEntity> = consumerStateService.getConsumers()

    @PostMapping
    fun addConsumer(
        @RequestBody consumerRequest: ConsumerRequest,
        webExchange: ServerWebExchange
    ): ResponseEntity<ConsumerEntity> =
        consumerStateService.saveConsumer(consumerRequest).let { (entity, wasCreated) ->
            when (wasCreated) {
                true -> ResponseEntity.created(URI.create("${webExchange.request.uri}/${entity.id}")).body(entity)
                false -> ResponseEntity.status(HttpStatus.CONFLICT).body(entity)
            }
        }

    @PutMapping("/{id}")
    fun updateConsumer(
        @PathVariable id: String,
        @RequestBody consumerUpdateRequest: ConsumerUpdateRequest
    ): ResponseEntity<ConsumerEntity> {
        return consumerStateService.updateConsumer(id, consumerUpdateRequest).map { ResponseEntity.ok(it) }
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Consumer id not found: $id") }
    }

    @DeleteMapping("/{id}")
    fun deleteConsumer(@PathVariable id: String): ResponseEntity<Void> =
        consumerStateService.deleteConsumer(id).map { ResponseEntity.noContent().build<Void>() }
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    // TODO: Temporary, get rid of this in production
    @PatchMapping
    fun resetAllData() = consumerStateService.resetAllData()

}