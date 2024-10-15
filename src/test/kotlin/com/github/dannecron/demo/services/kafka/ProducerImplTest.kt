package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.BaseUnitTest
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import com.github.dannecron.demo.services.validation.SchemaValidator
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.Message
import org.springframework.test.context.junit4.SpringRunner
import java.time.OffsetDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest
class ProducerImplTest: BaseUnitTest() {
    @Autowired
    private lateinit var producerImpl: ProducerImpl

    @MockBean
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @MockBean
    private lateinit var schemaValidator: SchemaValidator

    @Test
    fun produceProductInfo_success() {
        val topic = "some-topic"
        val product = Product(
            id = 123,
            guid = UUID.randomUUID(),
            name = "name",
            description = null,
            price = 10050,
            createdAt = OffsetDateTime.now().minusDays(2),
            updatedAt = OffsetDateTime.now().minusHours(1),
            deletedAt = OffsetDateTime.now(),
        )

        val captor = argumentCaptor<Message<String>>()

        whenever(kafkaTemplate.send(captor.capture())) doReturn CompletableFuture<SendResult<String, Any>>()

        whenever(schemaValidator.validate(
            eq("product-sync"),
            eq(Json.encodeToJsonElement(product))
        )) doAnswer { }

        producerImpl.produceProductInfo(topic, product)

        assertEquals(1, captor.allValues.count())
        val actualArgument = captor.firstValue

        val actualProductDto = Json.decodeFromString<ProductDto>(actualArgument.payload)
        assertEquals(product.id, actualProductDto.id)
        assertEquals(product.guid.toString(), actualProductDto.guid)
        assertEquals(topic, actualArgument.headers[KafkaHeaders.TOPIC])
        assertEquals("some-custom-header", actualArgument.headers["X-Custom-Header"])
    }
}
