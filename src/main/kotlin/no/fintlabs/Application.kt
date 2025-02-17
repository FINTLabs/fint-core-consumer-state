package no.fintlabs

import no.fintlabs.webhook.server.annotation.WebhookServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@WebhookServer
@ConfigurationPropertiesScan
@SpringBootApplication
class FintCoreConsumerStateApplication

fun main(args: Array<String>) {
    runApplication<FintCoreConsumerStateApplication>(*args)
}
