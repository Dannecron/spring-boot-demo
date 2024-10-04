package com.example.demo.services.kafka

import io.github.optimumcode.json.schema.JsonSchema
import io.github.optimumcode.json.schema.ValidationError
import kotlinx.serialization.json.JsonElement
import org.springframework.util.ResourceUtils

class SchemaValidator(
    private val schemaMap: Map<String, String>,
) {
    private val loadedSchema: MutableMap<String, String> = mutableMapOf()

    fun validate(schemaName: String, value: JsonElement) {

        val schema = JsonSchema.fromDefinition(
            getSchema(schemaName),
        )

        val errors = mutableListOf<ValidationError>()

        val valid = schema.validate(value, errors::add)
        if (!valid) {
            // todo throw another exception
            println(errors.toString())

           throw RuntimeException("invalid schema")
        }
    }

    private fun getSchema(schemaName: String): String {
        val loaded = loadedSchema[schemaName]
        if (loaded != null) {
            return loaded
        }

        val schemaFile = schemaMap[schemaName]
            ?: // todo throw another exception
            throw RuntimeException("unknown json-schema")

        val schema = ResourceUtils.getFile("classpath:json-schemas/$schemaFile")
            .readText(Charsets.UTF_8)
        loadedSchema[schemaName] = schema

        return schema
    }
}