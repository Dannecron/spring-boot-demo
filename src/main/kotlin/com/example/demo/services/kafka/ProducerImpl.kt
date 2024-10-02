package com.example.demo.services.kafka

import com.example.demo.models.Product
import com.example.demo.services.kafka.dto.ProductDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class ProducerImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper
): Producer {
    override fun produceProductInfo(topicName: String, product: Product) {
        val message: Message<String> = MessageBuilder
            .withPayload(objectMapper.writeValueAsString(ProductDto(product)))
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader("X-Custom-Header", "some-custom-header")
            .build()

        kafkaTemplate.send(message)
    }
}