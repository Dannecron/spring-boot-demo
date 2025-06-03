package com.github.dannecron.demo.edgecontracts.validation.config

import com.github.dannecron.demo.edgecontracts.validation.SchemaValidator
import com.github.dannecron.demo.edgecontracts.validation.SchemaValidatorImp
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils

@Configuration
@EnableConfigurationProperties(ValidationProperties::class)
class SchemaValidationConfig(
    private val validationProperties: ValidationProperties,
) {

    @Bean
    fun schemaValidator(): SchemaValidator = SchemaValidatorImp(
        schemaMap = validationProperties.schema.mapValues {
            schema -> ResourceUtils.getFile("classpath:json-schemas/${schema.value}")
                .readText(Charsets.UTF_8)
        }
    )
}
