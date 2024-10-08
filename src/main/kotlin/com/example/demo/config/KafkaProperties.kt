package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("kafka")
data class KafkaProperties @ConstructorBinding constructor(
    val bootstrapServers: String,
    val producer: Producer,
    val consumer: Consumer,
    val validation: Validation,
) {
    data class Producer(
        val product: Product,
    ) {
        data class Product(
            val defaultSyncTopic: String
        )
    }

    data class Consumer(
        val groupId: String,
        val topics: String,
        val autoStartup: Boolean,
        val autoOffsetReset: String,
    )

    data class Validation(
        val schema: Map<String, String>
    )
}