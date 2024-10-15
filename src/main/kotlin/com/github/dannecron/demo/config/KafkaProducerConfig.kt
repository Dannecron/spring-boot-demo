package com.github.dannecron.demo.config

import com.github.dannecron.demo.config.properties.KafkaProperties
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.kafka.ProducerImpl
import com.github.dannecron.demo.services.validation.SchemaValidator
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig(
    @Autowired val kafkaProperties: KafkaProperties
) {
    @Bean
    fun producerFactory(): ProducerFactory<String, Any> = DefaultKafkaProducerFactory(mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
    ))

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(
        producerFactory(),
    )

    @Bean
    fun producer(
        @Autowired kafkaTemplate: KafkaTemplate<String, Any>,
        @Autowired schemaValidator: SchemaValidator,
    ): Producer = ProducerImpl(
        kafkaTemplate,
        schemaValidator,
    )
}
