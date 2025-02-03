package no.fintlabs.consumer.state.repository

import org.springframework.stereotype.Service

@Service
class ConsumerStateRepository(
    val cache: MutableMap<String, ConsumerEntity> = mutableMapOf()
)