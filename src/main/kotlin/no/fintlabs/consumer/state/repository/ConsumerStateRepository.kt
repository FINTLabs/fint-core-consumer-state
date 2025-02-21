package no.fintlabs.consumer.state.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ConsumerStateRepository : ReactiveCrudRepository<ConsumerEntity, String> {
}