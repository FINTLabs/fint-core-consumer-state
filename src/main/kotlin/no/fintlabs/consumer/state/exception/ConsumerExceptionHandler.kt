package no.fintlabs.consumer.state.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ConsumerExceptionHandler {

    @ExceptionHandler(InvalidConsumerException::class)
    fun handleConsumerValidationException(exception: InvalidConsumerException): ResponseEntity<String?> =
        ResponseEntity.badRequest().body(exception.message)

}