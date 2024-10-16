package no.fintlabs.consumer.state

data class ConsumerState(
    val domain: String,
    val `package`: String,
    val org: String,
    val version: String,
    var managed: Boolean
)
