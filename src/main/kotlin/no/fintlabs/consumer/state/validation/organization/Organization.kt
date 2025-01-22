package no.fintlabs.consumer.state.validation.organization

import com.fasterxml.jackson.annotation.JsonProperty

data class Organization(
    @JsonProperty("primaryAssetId")
    val name: String
)
