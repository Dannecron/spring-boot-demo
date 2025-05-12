package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.core.services.validation.SchemaValidator
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.Message
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.Test

class ProducerImplTest {
    private val streamBridge: StreamBridge = mock()
    private val schemaValidator: SchemaValidator = mock()
    private val producerImpl = ProducerImpl(
        streamBridge = streamBridge,
        schemaValidator = schemaValidator,
    )

    @Test
    fun produceProductSync_success() {
        val guid = UUID.randomUUID()
        val createdAt = OffsetDateTime.now().minusDays(2)
        val updatedAt = OffsetDateTime.now().minusHours(1)
        val productDto = ProductDto(
            id = 123,
            guid = guid.toString(),
            name = "name",
            description = null,
            price = 10050,
            createdAt = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = updatedAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            deletedAt = null,
        )

        val captor = argumentCaptor<Message<String>>()

        whenever(streamBridge.send(any(), captor.capture())).thenReturn(true)

        producerImpl.produceProductSync(productDto)

        assertEquals(1, captor.allValues.count())
        val actualArgument = captor.firstValue

        val actualProductDto = Json.decodeFromString<ProductDto>(actualArgument.payload)
        assertEquals(productDto, actualProductDto)
        assertEquals("some-custom-header", actualArgument.headers["X-Custom-Header"])

        verify(streamBridge, times(1)).send(eq("productSyncProducer"), any())
        verify(schemaValidator, times(1)).validate("kafka-product-sync", Json.encodeToJsonElement(productDto))
    }
}
