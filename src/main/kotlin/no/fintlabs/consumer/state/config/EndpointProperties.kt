package no.fintlabs.consumer.state.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("fint.endpoints")
data class EndpointProperties(
    val admin: String,
    val metamodel: String
)
