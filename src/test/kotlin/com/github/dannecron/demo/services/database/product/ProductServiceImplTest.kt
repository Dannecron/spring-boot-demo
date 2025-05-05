package com.github.dannecron.demo.services.database.product

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.Product
import com.github.dannecron.demo.db.repository.ProductRepository
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.ProductNotFoundException
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.assertEquals

class ProductServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val productRepository: ProductRepository = mock()
    private val producer: Producer = mock()
    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val productService = ProductServiceImpl(
        productRepository = productRepository,
        producer = producer,
        commonGenerator = commonGenerator,
    )

    private val guid = UUID.randomUUID()
    private val product = Product(
        id = 123,
        guid = guid,
        name = "name",
        description = "description",
        price = 10050,
        createdAt = OffsetDateTime.now().minusDays(1),
        updatedAt = OffsetDateTime.now().minusHours(2),
        deletedAt = null,
    )
    private val kafkaProductDto = ProductDto(
        id = 123,
        guid = guid.toString(),
        name = "name",
        description = "description",
        price = 10050,
        createdAt = product.createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = product.updatedAt!!.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        deletedAt = null,
    )

    @Test
    fun create() {
        val expectedProductForCreation = product.copy(
            id = null,
            guid = mockGuid,
            createdAt = mockCurrentTime,
            updatedAt = null,
        )
        val expectedCreatedProduct = expectedProductForCreation.copy(id = 1)

        whenever(productRepository.save<Product>(any())).thenReturn(expectedCreatedProduct)

        val result = productService.create(
            name = "name",
            price = 10050,
            description = "description",
        )
        assertEquals(expectedCreatedProduct, result)

        verify(productRepository, times(1)).save(expectedProductForCreation)
    }

    @Test
    fun `delete - success`() {
        val deletedProduct = product.copy(
            deletedAt = mockCurrentTime,
        )
        whenever(productRepository.findByGuid(any())).thenReturn(product)
        whenever(productRepository.save<Product>(any())).thenReturn(deletedProduct)

        val result = productService.delete(guid)
        assertEquals(deletedProduct, result)

        verify(productRepository, times(1)).findByGuid(guid)
        verify(productRepository, times(1)).save(deletedProduct)
    }

    @Test
    fun `delete - fail - already deleted`() {
        val deletedProduct = product.copy(
            deletedAt = mockCurrentTime,
        )
        whenever(productRepository.findByGuid(any())).thenReturn(deletedProduct)

        assertThrows<AlreadyDeletedException> {
            productService.delete(guid)
        }

        verify(productRepository, times(1)).findByGuid(guid)
        verify(productRepository, never()).save(any())
    }

    @Test
    fun `delete - fail - not found`() {
        whenever(productRepository.findByGuid(any())).thenReturn(null)

        assertThrows<ProductNotFoundException> {
            productService.delete(guid)
        }

        verify(productRepository, times(1)).findByGuid(guid)
        verify(productRepository, never()).save(any())
    }

    @Test
    fun `syncToKafka - success`() {
        whenever(productRepository.findByGuid(any())) doReturn product

        productService.syncToKafka(guid, null)

        verify(productRepository, times(1)).findByGuid(guid)
        verify(producer, times(1)).produceProductSync(kafkaProductDto)
    }

    @Test
    fun `syncToKafka - not found`() {
        whenever(productRepository.findByGuid(any())) doReturn null

        assertThrows<ProductNotFoundException> {
            productService.syncToKafka(guid, null)
        }

        verify(productRepository, times(1)).findByGuid(guid)
        verifyNoInteractions(producer)
    }
}
