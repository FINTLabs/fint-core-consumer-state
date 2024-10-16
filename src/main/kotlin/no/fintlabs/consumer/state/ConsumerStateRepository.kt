package no.fintlabs.consumer.state

import org.springframework.data.jpa.repository.JpaRepository

interface ConsumerStateRepository : JpaRepository<ConsumerState, String> {
}