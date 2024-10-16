package no.fintlabs.consumer.state

import no.fintlabs.consumer.state.model.ConsumerState
import org.springframework.data.jpa.repository.JpaRepository

interface ConsumerStateRepository : JpaRepository<ConsumerState, String> {
}