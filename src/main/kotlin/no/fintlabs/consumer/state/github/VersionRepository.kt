package no.fintlabs.consumer.state.github

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository

@Repository
class VersionRepository(
    val versionFetcher: VersionFetcher
) {

    val versions: MutableSet<String> = hashSetOf()

    fun versionExists(version: String) = versions.contains(version)

    @PostConstruct
    fun populateVersions() =
        versionFetcher.getReleases().subscribe{
            it.forEach { release -> versions.add(release.name) }
        }

}