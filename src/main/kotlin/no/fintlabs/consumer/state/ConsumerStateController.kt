package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.service.ConsumerStateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ConsumerStateController(
    val consumerStateService: ConsumerStateService
) {

    @GetMapping
    fun getStates(): Collection<ConsumerState> =
        consumerStateService.getConsumerStates()

    @PostMapping
    fun addConsumerState(@RequestBody consumerState: ConsumerState) =
        consumerStateService.addConsumerState(consumerState)

    @PutMapping("/{id}")
    fun updateConsumerState(@RequestBody consumerState: ConsumerState, @PathVariable id: String) =
        consumerStateService.updateConsumerState(consumerState, id)
    fun updateConsumerState(@RequestBody consumerStateUpdate: ConsumerStateUpdate, @PathVariable id: String): ConsumerState =
        consumerStateService.updateConsumerState(consumerStateUpdate, id)
            .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }


}