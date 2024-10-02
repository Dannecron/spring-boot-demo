package com.example.demo.config

import com.example.demo.services.kafka.Producer
import com.example.demo.services.kafka.ProducerImpl
import com.example.demo.services.kafka.dto.serializer.ProductSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class ProducerConfig(
    @Value("\${kafka.bootstrap-servers}")
    val servers: String
) {
    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()

        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = ProductSerializer::class.java

        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(
        producerFactory(),
    )

    @Bean
    fun producer(@Autowired kafkaTemplate: KafkaTemplate<String, Any>): Producer = ProducerImpl(
        kafkaTemplate,
    )
}