package com.github.dannecron.demo.edgecontracts.validation.config

import com.github.dannecron.demo.edgecontracts.validation.SchemaValidator
import com.github.dannecron.demo.edgecontracts.validation.SchemaValidatorImp
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
@EnableConfigurationProperties(ValidationProperties::class)
class SchemaValidationConfig(
    private val validationProperties: ValidationProperties,
    private val resourceLoader: ResourceLoader,
) {

    @Bean
    fun schemaValidator(): SchemaValidator = SchemaValidatorImp(
        schemaMap = validationProperties.schema.mapValues {
            schema -> resourceLoader.getResource("classpath:json-schemas/${schema.value}")
                .takeIf { it.exists() }!!
                .getContentAsString(Charsets.UTF_8)
        }
    )
}
