package com.example.demo

import com.example.demo.config.KafkaProperties
import com.example.demo.services.kafka.Consumer
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
            ),
            validation = KafkaProperties.Validation(
                schema = mapOf("product-sync" to "foo"),
            ),
        )
    }
}