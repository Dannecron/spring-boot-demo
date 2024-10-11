package com.example.demo

import com.example.demo.config.properties.KafkaProperties
import com.example.demo.config.properties.ValidationProperties
import com.example.demo.services.kafka.Consumer
import com.example.demo.services.validation.SchemaValidator.Companion.SCHEMA_KAFKA_PRODUCT_SYNC
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean

open class BaseUnitTest {
    @MockBean
    lateinit var consumer: Consumer

    @TestConfiguration
    class TestConfig {
        @Bean
        fun kafkaProperties(): KafkaProperties = KafkaProperties(
            bootstrapServers = "localhost:1111",
            producer = KafkaProperties.Producer(
                product = KafkaProperties.Producer.Product(
                    defaultSyncTopic = "some-default",
                ),
            ),
            consumer = KafkaProperties.Consumer(
                groupId = "group",
                topics = "topic",
                autoStartup = false,
                autoOffsetReset = "none",
            ),
        )

        @Bean
        fun validationProperties(): ValidationProperties = ValidationProperties(
            schema = mapOf(SCHEMA_KAFKA_PRODUCT_SYNC to "kafka/product/sync.json"),
        )
    }
}