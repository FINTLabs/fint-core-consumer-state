package no.fintlabs.consumer.state.repository

import org.springframework.data.jpa.repository.JpaRepository

interface ConsumerStateRepository : JpaRepository<ConsumerEntity, String> {
}