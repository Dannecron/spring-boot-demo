package com.github.dannecron.demo.services

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.core.services.product.ProductService
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ProductSyncServiceImplTest {
    private val productService: ProductService = mock()
    private val producer: Producer = mock()

    private val productSyncService = ProductSyncServiceImpl(
        productService = productService,
        producer = producer
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
    fun `syncToKafka - success`() {
        whenever(productService.findByGuid(any())).thenReturn(product)

        productSyncService.syncToKafka(guid, null)

        verify(productService, times(1)).findByGuid(guid)
        verify(producer, times(1)).produceProductSync(kafkaProductDto)
    }

    @Test
    fun `syncToKafka - not found`() {
        whenever(productService.findByGuid(any())) doReturn null

        assertThrows<ProductNotFoundException> {
            productSyncService.syncToKafka(guid, null)
        }

        verify(productService, times(1)).findByGuid(guid)
        verifyNoInteractions(producer)
    }
}
