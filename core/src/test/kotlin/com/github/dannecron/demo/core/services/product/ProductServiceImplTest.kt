package com.github.dannecron.demo.core.services.product

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.ProductEntity
import com.github.dannecron.demo.db.repository.ProductRepository
import com.github.dannecron.demo.edgeproducing.dto.ProductDto
import com.github.dannecron.demo.edgeproducing.producer.ProductProducer
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
    private val productProducer: ProductProducer = mock()
    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val productService = ProductServiceImpl(
        productRepository = productRepository,
        commonGenerator = commonGenerator,
        productProducer = productProducer,
    )

    private val guid = UUID.randomUUID()
    private val productEntity = ProductEntity(
        id = 123,
        guid = guid,
        name = "name",
        description = "description",
        price = 10050,
        createdAt = mockCurrentTime.minusDays(1),
        updatedAt = mockCurrentTime.minusHours(2),
        deletedAt = null,
    )
    private val product = Product(
        id = 123,
        guid = guid,
        name = "name",
        description = "description",
        price = 10050,
        createdAt = mockCurrentTime.minusDays(1),
        updatedAt = mockCurrentTime.minusHours(2),
        deletedAt = null,
    )
    private val producingProductDto = ProductDto(
        id = 123,
        guid = guid.toString(),
        name = "name",
        description = "description",
        price = 10050,
        createdAt = mockCurrentTime.minusDays(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = mockCurrentTime.minusHours(2).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        deletedAt = null,
    )

    @Test
    fun create() {
        val expectedProductForCreation = productEntity.copy(
            id = null,
            guid = mockGuid,
            createdAt = mockCurrentTime,
            updatedAt = null,
        )
        val expectedCreatedProductEntity = expectedProductForCreation.copy(id = 1)
        val expectedProduct = product.copy(
            id = 1,
            guid = mockGuid,
            createdAt = mockCurrentTime,
            updatedAt = null,
        )

        whenever(productRepository.save<ProductEntity>(any())).thenReturn(expectedCreatedProductEntity)

        val result = productService.create(
            name = "name",
            price = 10050,
            description = "description",
        )
        assertEquals(expectedProduct, result)

        verify(productRepository, times(1)).save(expectedProductForCreation)
    }

    @Test
    fun `delete - success`() {
        val deletedProductEntity = productEntity.copy(
            deletedAt = mockCurrentTime,
        )
        val expectedProduct = product.copy(deletedAt = mockCurrentTime)

        whenever(productRepository.findByGuid(any())).thenReturn(productEntity)
        whenever(productRepository.save<ProductEntity>(any())).thenReturn(deletedProductEntity)

        val result = productService.delete(guid)
        assertEquals(expectedProduct, result)

        verify(productRepository, times(1)).findByGuid(guid)
        verify(productRepository, times(1)).save(deletedProductEntity)
    }

    @Test
    fun `delete - fail - already deleted`() {
        val deletedProduct = productEntity.copy(
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
    fun `send - success`() {
        val topic = "some-topic"

        whenever(productRepository.findByGuid(any())).thenReturn(productEntity)

        productService.send(guid, topic)

        verify(productRepository, times(1)).findByGuid(guid)
        verify(productProducer, times(1)).produceProductSync(producingProductDto)
    }

    @Test
    fun `send - not found`() {
        val topic = "some-topic"

        whenever(productRepository.findByGuid(any())).thenReturn(null)

        assertThrows<ProductNotFoundException> {
            productService.send(guid, topic)
        }

        verify(productRepository, times(1)).findByGuid(guid)
        verifyNoInteractions(productProducer)
    }
}
