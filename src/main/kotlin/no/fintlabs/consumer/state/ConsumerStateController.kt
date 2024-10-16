package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerState
import no.fintlabs.consumer.state.model.ConsumerStateUpdate
import no.fintlabs.consumer.state.service.ConsumerStateService
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.ServerHttpRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.net.URI

@RestController
class ConsumerStateController(
    val consumerStateService: ConsumerStateService
) {

    @GetMapping
    fun getStates(): Collection<ConsumerState> = consumerStateService.getConsumerStates()

    @PostMapping
    fun addConsumerState(@RequestBody consumerState: ConsumerState, request: ServerHttpRequest): ResponseEntity<ConsumerState> =
        consumerStateService.addConsumerState(consumerState).let {
            ResponseEntity.created(URI.create("${request.uri}/${it.id}")).body(it)
        }


    @PutMapping("/{id}")
    fun updateConsumerState(@RequestBody consumerStateUpdate: ConsumerStateUpdate, @PathVariable id: String): ConsumerState =
        consumerStateService.updateConsumerState(consumerStateUpdate, id)
            .orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

    @DeleteMapping("/{id}")
    fun deleteConsumerState(@PathVariable id: String): ConsumerState =
        consumerStateService.deleteConsumerState(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

}