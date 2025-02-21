package no.fintlabs.consumer.state.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fint.consumer.state.validation")
data class ConsumerStateValidationProperties(
    val organisation: Boolean = true
)