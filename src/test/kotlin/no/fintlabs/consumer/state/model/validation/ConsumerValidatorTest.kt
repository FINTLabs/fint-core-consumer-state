package no.fintlabs.consumer.state.model.validation

import no.fintlabs.consumer.state.exception.InvalidConsumerException
import no.fintlabs.consumer.state.github.VersionRepository
import no.fintlabs.consumer.state.metadata.MetadataRepository
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.ConsumerUpdateRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class ConsumerValidatorTest {

    private lateinit var consumerValidator: ConsumerValidator

    private val metadataRepository: MetadataRepository = mock(MetadataRepository::class.java)
    private val versionRepository: VersionRepository = mock(VersionRepository::class.java)

    @BeforeEach
    fun setUp() {
        consumerValidator = ConsumerValidator(metadataRepository, versionRepository)
    }

    @Test
    fun `validateRequest with valid ConsumerRequest`() {
        val consumerRequest = ConsumerRequest(
            domain = "TestDomain",
            `package` = "TestPackage",
            org = "TestOrg",
            version = "1.0.0",
            resources = listOf("Resource1", "Resource2"),
            writeableResources = listOf("Resource1"),
            cacheDisabledResources = listOf("Resource2"),
            managed = true
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("1.0.0")).thenReturn(true)
        `when`(metadataRepository.resourceBelongsToComponent(any(), any(), any())).thenReturn(true)

        assertDoesNotThrow {
            consumerValidator.validateRequest(consumerRequest)
        }
    }

    @Test
    fun `validateRequest with invalid component throws exception`() {

        val consumerRequest = ConsumerRequest(
            domain = "InvalidDomain",
            `package` = "InvalidPackage",
            org = "TestOrg",
            version = "1.0.0"
        )

        `when`(metadataRepository.containsComponent("InvalidDomain", "InvalidPackage")).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateRequest(consumerRequest)
        }
        assertEquals("Component: 'InvalidDomain-InvalidPackage' does not exist", exception.message)
    }

    @Test
    fun `validateRequest with invalid version throws exception`() {

        val consumerRequest = ConsumerRequest(
            domain = "TestDomain",
            `package` = "TestPackage",
            org = "TestOrg",
            version = "InvalidVersion"
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("InvalidVersion")).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateRequest(consumerRequest)
        }
        assertEquals("Version: InvalidVersion does not exist", exception.message)
    }

    @Test
    fun `validateRequest with invalid resource throws exception`() {

        val consumerRequest = ConsumerRequest(
            domain = "TestDomain",
            `package` = "TestPackage",
            org = "TestOrg",
            version = "1.0.0",
            resources = listOf("InvalidResource")
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("1.0.0")).thenReturn(true)
        `when`(metadataRepository.resourceBelongsToComponent("TestDomain", "TestPackage", "InvalidResource")).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateRequest(consumerRequest)
        }
        assertEquals(
            "Resource: InvalidResource in field: resources does not exist in 'TestDomain-TestPackage' component",
            exception.message
        )
    }

    @Test
    fun `validateUpdateRequest with valid data`() {

        val id = "TestDomain-TestPackage-TestOrg"
        val consumerUpdateRequest = ConsumerUpdateRequest(
            version = "1.0.0",
            managed = true,
            resources = listOf("Resource1"),
            writeableResources = listOf("Resource2"),
            cacheDisabledResources = listOf("Resource3")
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("1.0.0")).thenReturn(true)
        `when`(metadataRepository.resourceBelongsToComponent(any(), any(), any())).thenReturn(true)

        assertDoesNotThrow {
            consumerValidator.validateUpdateRequest(id, consumerUpdateRequest)
        }
    }

    @Test
    fun `validateUpdateRequest with invalid id throws exception`() {

        val id = "InvalidDomain-InvalidPackage-TestOrg"
        val consumerUpdateRequest = ConsumerUpdateRequest(
            version = "1.0.0",
            managed = true,
            resources = listOf("Resource1"),
            writeableResources = listOf("Resource2"),
            cacheDisabledResources = listOf("Resource3")
        )

        `when`(metadataRepository.containsComponent("InvalidDomain", "InvalidPackage")).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateUpdateRequest(id, consumerUpdateRequest)
        }
        assertEquals("Component: 'InvalidDomain-InvalidPackage' does not exist", exception.message)
    }

    @Test
    fun `validateRequest with invalid writeableResource throws exception`() {

        val consumerRequest = ConsumerRequest(
            domain = "TestDomain",
            `package` = "TestPackage",
            org = "TestOrg",
            version = "1.0.0",
            writeableResources = listOf("InvalidWriteableResource")
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("1.0.0")).thenReturn(true)
        `when`(
            metadataRepository.resourceBelongsToComponent(
                "TestDomain",
                "TestPackage",
                "InvalidWriteableResource"
            )
        ).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateRequest(consumerRequest)
        }
        assertEquals(
            "Resource: InvalidWriteableResource in field: writeableResources does not exist in 'TestDomain-TestPackage' component",
            exception.message
        )
    }

    @Test
    fun `validateRequest with invalid cacheDisabledResource throws exception`() {

        val consumerRequest = ConsumerRequest(
            domain = "TestDomain",
            `package` = "TestPackage",
            org = "TestOrg",
            version = "1.0.0",
            cacheDisabledResources = listOf("InvalidCacheDisabledResource")
        )

        `when`(metadataRepository.containsComponent("TestDomain", "TestPackage")).thenReturn(true)
        `when`(versionRepository.versionExists("1.0.0")).thenReturn(true)
        `when`(
            metadataRepository.resourceBelongsToComponent(
                "TestDomain",
                "TestPackage",
                "InvalidCacheDisabledResource"
            )
        ).thenReturn(false)

        val exception = assertThrows(InvalidConsumerException::class.java) {
            consumerValidator.validateRequest(consumerRequest)
        }
        assertEquals(
            "Resource: InvalidCacheDisabledResource in field: cacheDisabledResources does not exist in 'TestDomain-TestPackage' component",
            exception.message
        )
    }
}
