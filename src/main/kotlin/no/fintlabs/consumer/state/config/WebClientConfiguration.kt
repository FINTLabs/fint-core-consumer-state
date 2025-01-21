package no.fintlabs.consumer.state.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration(
    @Value("\${fint.metamodel}") private val metamodelUrl: String
) {

    @Bean("metamodelWebClient")
    fun metamodelWebClient(): WebClient = WebClient.create(metamodelUrl)
}