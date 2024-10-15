package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import com.github.dannecron.demo.services.validation.SchemaValidator
import com.github.dannecron.demo.services.validation.SchemaValidator.Companion.SCHEMA_KAFKA_PRODUCT_SYNC
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class ProducerImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val schemaValidator: SchemaValidator,
): Producer {
    override fun produceProductInfo(topicName: String, product: Product) {

        val serializedProduct = Json.encodeToJsonElement(ProductDto(product))

        schemaValidator.validate(SCHEMA_KAFKA_PRODUCT_SYNC, serializedProduct)

        val message: Message<String> = MessageBuilder
            .withPayload(serializedProduct.toString())
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader("X-Custom-Header", "some-custom-header")
            .build()

        kafkaTemplate.send(message)
    }
}
