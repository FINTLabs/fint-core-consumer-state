package no.fintlabs.consumer.state.metadata

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class MetadataRepository(
    val metadataFetcher: MetadataFetcher
) {

    val componentToResourceMap: ConcurrentHashMap<String, MutableSet<String>> = ConcurrentHashMap()

    @PostConstruct
    fun populateComponentToResourceMap() =
        metadataFetcher.getMetadata().subscribe {
            if (it.packageName != null) {
                componentToResourceMap.computeIfAbsent(createComponentKey(it.domainName, it.packageName)) { mutableSetOf() }
                    .add(it.resourceName)
            }
        }

    fun containsComponent(domain: String, `package`: String) =
        componentToResourceMap.containsKey(createComponentKey(domain, `package`))

    fun resourceBelongsToComponent(domain: String, `package`: String, resource: String) =
        componentToResourceMap.getOrDefault(createComponentKey(domain, `package`), mutableSetOf()).contains(resource.lowercase())

    private fun createComponentKey(domain: String, `package`: String) = "${domain.lowercase()}-${`package`.lowercase()}"

}