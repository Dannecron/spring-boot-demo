package com.github.dannecron.demo

import com.github.dannecron.demo.core.config.properties.ValidationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

open class BaseUnitTest {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun validationProperties(): ValidationProperties = ValidationProperties(
            schema = mapOf("kafka-product-sync" to "kafka/product/sync.json"),
        )
    }
}
