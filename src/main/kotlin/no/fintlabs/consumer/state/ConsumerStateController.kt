package no.fintlabs.consumer.state

import kotlinx.coroutines.flow.Flow
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity
import no.fintlabs.consumer.state.validation.ConsumerValidationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.net.URI

@RestController
@RequestMapping("/consumer")
class ConsumerStateController(
    private val consumerStateService: ConsumerStateService,
    private val consumerValidationService: ConsumerValidationService
) {

    @GetMapping
    fun getConsumers(): Flow<ConsumerEntity> =
        consumerStateService.getConsumers()

    @GetMapping("/{id}")
    suspend fun getConsumer(@PathVariable id: String) =
        consumerStateService.getConsumer(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound()

    @PostMapping
    suspend fun addConsumer(
        @RequestBody consumerRequest: ConsumerRequest,
        webExchange: ServerWebExchange
    ): ResponseEntity<ConsumerEntity> =
        consumerValidationService.validateRequest(consumerRequest).takeIf { it }?.let {
            val (entity, wasCreated) = consumerStateService.saveConsumer(consumerRequest)

            return if (wasCreated) ResponseEntity.created(createUri(webExchange, entity)).body(entity)
            else ResponseEntity.status(HttpStatus.CONFLICT).body(entity)
        } ?: ResponseEntity.badRequest().build()

    @PutMapping("/{id}")
    suspend fun updateConsumer(
        @PathVariable id: String,
        @RequestBody consumerUpdateRequest: ConsumerUpdateRequest
    ): ResponseEntity<ConsumerEntity> =
        consumerValidationService.validateConsumerFields(id, consumerUpdateRequest).takeIf { it }?.let {
            consumerStateService.updateConsumer(id, consumerUpdateRequest)?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.notFound().build()
        } ?: ResponseEntity.badRequest().build()

    @DeleteMapping("/{id}")
    suspend fun deleteConsumer(@PathVariable id: String): ResponseEntity<Void> =
        consumerStateService.deleteConsumer(id)?.let { ResponseEntity.noContent().build() }
            ?: ResponseEntity.notFound().build()

    private fun createUri(webExchange: ServerWebExchange, entity: ConsumerEntity) =
        URI.create("${webExchange.request.uri}/${entity.id}")

}