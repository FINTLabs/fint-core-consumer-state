package no.fintlabs.consumer.state.github

import com.fasterxml.jackson.annotation.JsonProperty

data class Release(
    val id: Long,
    @JsonProperty("tag_name")
    val tagName: String,
    val name: String,
)
