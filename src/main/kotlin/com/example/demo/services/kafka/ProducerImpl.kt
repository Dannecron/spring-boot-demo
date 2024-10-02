package com.example.demo.services.kafka

import com.example.demo.models.Product
import com.example.demo.services.kafka.dto.ProductDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class ProducerImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>
): Producer {
    override fun produceProductInfo(topicName: String, product: Product) {
        val message: Message<ProductDto> = MessageBuilder
            .withPayload(ProductDto(product))
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader("X-Custom-Header", "some-custom-header")
            .build()

        kafkaTemplate.send(message)
    }
}