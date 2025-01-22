package no.fintlabs.consumer.state.validation.github

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository

@Repository
class VersionRepository(
    val versionFetcher: VersionFetcher
) {

    private val versions: MutableSet<String> = hashSetOf()

    fun versionExists(version: String) = versions.contains(version)

    @PostConstruct
    fun populateVersions() =
        versionFetcher.getReleases().subscribe{
            it.forEach { release -> versions.add(release.name.replace("v", "")) }
        }

}