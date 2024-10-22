package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ConsumerStateRepository : JpaRepository<ConsumerEntity, String> {
}