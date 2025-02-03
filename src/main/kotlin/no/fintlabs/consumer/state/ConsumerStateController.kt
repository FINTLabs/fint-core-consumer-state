package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.repository.ConsumerEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/consumer")
class ConsumerStateController(
    private val consumerStateService: ConsumerStateService
) {

    @GetMapping
    fun getConsumers(): Collection<ConsumerEntity> = consumerStateService.getConsumers()

    @PostMapping
    fun addConsumer(
        @RequestBody consumerRequest: ConsumerRequest,
        webExchange: ServerWebExchange
    ): ResponseEntity<ConsumerEntity> =
        consumerStateService.saveConsumer(consumerRequest).let {
            ResponseEntity.ok(it)
        }

    // TODO: Temporary, get rid of this in production
    @PatchMapping
    fun resetAllData() = consumerStateService.resetAllData()

}