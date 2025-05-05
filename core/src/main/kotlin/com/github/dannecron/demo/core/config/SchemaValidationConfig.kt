package com.github.dannecron.demo.core.config

import com.github.dannecron.demo.core.config.properties.ValidationProperties
import com.github.dannecron.demo.core.services.validation.SchemaValidator
import com.github.dannecron.demo.core.services.validation.SchemaValidatorImp
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils

@Configuration
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
