package no.fintlabs.consumer.state

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FintCoreConsumerStateApplication

fun main(args: Array<String>) {
	runApplication<FintCoreConsumerStateApplication>(*args)
}
