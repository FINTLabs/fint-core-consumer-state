package no.fintlabs.consumer.state.model

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class ConsumerState(
    val domain: String,
    val `package`: String,
    val org: String,
    val version: String,
    var managed: Boolean? = true,

    @ElementCollection
    val writeable: List<String> = mutableListOf()
) {
    @Id
    val id: String = UUID.randomUUID().toString()
}
