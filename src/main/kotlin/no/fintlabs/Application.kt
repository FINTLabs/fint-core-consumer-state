package no.fintlabs

import no.fintlabs.webhook.server.annotation.WebHookServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@WebHookServer
@SpringBootApplication
class FintCoreConsumerStateApplication

fun main(args: Array<String>) {
    runApplication<FintCoreConsumerStateApplication>(*args)
}
