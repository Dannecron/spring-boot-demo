package com.example.demo.services.database.product

import com.example.demo.models.Product
import com.example.demo.providers.ProductRepository
import com.example.demo.services.database.product.exceptions.ProductNotFoundException
import com.example.demo.services.kafka.Producer
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(SpringRunner::class)
@SpringBootTest
class ProductServiceImplTest {
    private val defaultTopic = "some-default-topic"
    private lateinit var productService: ProductServiceImpl

    @MockBean
    @Qualifier("producer")
    private lateinit var producer: Producer
    @MockBean
    private lateinit var productRepository: ProductRepository

    @BeforeTest
    fun setUp() {
        productService = ProductServiceImpl(
            defaultSyncTopic = defaultTopic,
            productRepository = productRepository,
            producer = producer,
        )
    }

    @Test
    fun syncToKafka_success() {
        val guid = UUID.randomUUID()
        val product = Product(
            id = 123,
            guid = guid,
            name = "name",
            description = "description",
            price = 10050,
            createdAt = OffsetDateTime.now().minusDays(1),
            updatedAt = OffsetDateTime.now().minusHours(2),
            deletedAt = OffsetDateTime.now(),
        )

        whenever(productRepository.findByGuid(eq(guid)))
            .thenReturn(product)
        whenever(producer.produceProductInfo(defaultTopic, product)).doAnswer{}

        productService.syncToKafka(guid, null)
    }

    @Test
    fun syncToKafka_notFound() {
        val specificTopic = "specificNotice"
        val guid = UUID.randomUUID()

        whenever(productRepository.findByGuid(eq(guid)))
            .thenReturn(null)

        assertThrows<ProductNotFoundException> {
            productService.syncToKafka(guid, specificTopic)
        }

        verifyNoInteractions(producer)
    }

    @Test
    fun syncToKafka_invalidArgumentException() {
        val specificTopic = "specificNotice"
        val guid = UUID.randomUUID()

        val product = Product(
            id = 123,
            guid = guid,
            name = "name",
            description = "description",
            price = 10050,
            createdAt = OffsetDateTime.now().minusDays(1),
            updatedAt = OffsetDateTime.now().minusHours(2),
            deletedAt = OffsetDateTime.now(),
        )

        whenever(productRepository.findByGuid(eq(guid)))
            .thenReturn(product)
        whenever(producer.produceProductInfo(specificTopic, product))
            .doThrow(InvalidArgumentException("some error"))

        assertThrows< InvalidArgumentException> {
            productService.syncToKafka(guid, specificTopic)
        }
    }
}