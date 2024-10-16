package no.fintlabs.consumer.state

data class ConsumerState(
    val domain: String,
    val `package`: String,
    val org: String,
    val version: String,
    val env: Map<String, String> = mutableMapOf(),
    var managed: Boolean? = true,
)