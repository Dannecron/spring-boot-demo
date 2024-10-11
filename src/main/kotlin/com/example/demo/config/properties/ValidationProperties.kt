package com.example.demo.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("validation")
data class ValidationProperties(
    val schema: Map<String, String>
)