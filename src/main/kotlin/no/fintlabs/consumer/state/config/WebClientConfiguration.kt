package no.fintlabs.consumer.state.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration(private val endpoints: EndpointProperties) {

    @Bean("metamodelWebClient")
    fun metamodelWebClient(): WebClient = WebClient.create(endpoints.metamodel)

    @Bean("adminWebClient")
    fun adminWebClient(): WebClient = WebClient.create(endpoints.admin)

}