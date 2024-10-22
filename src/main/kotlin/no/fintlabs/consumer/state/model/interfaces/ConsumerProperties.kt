package no.fintlabs.consumer.state.model.interfaces

interface ConsumerProperties {
    val version: String?
    val managed: Boolean?
    val resources: List<String>?
    val writeableResources: List<String>?
    val cacheDisabledResources: List<String>?
}