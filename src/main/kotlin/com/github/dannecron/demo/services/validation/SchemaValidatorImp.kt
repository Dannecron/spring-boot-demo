package com.github.dannecron.demo.services.validation

import com.github.dannecron.demo.services.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.services.validation.exceptions.SchemaNotFoundException
import io.github.optimumcode.json.schema.JsonSchema
import io.github.optimumcode.json.schema.ValidationError
import kotlinx.serialization.json.JsonElement
import org.springframework.util.ResourceUtils

class SchemaValidatorImp(
    private val schemaMap: Map<String, String>,
): SchemaValidator {
    private val loadedSchema: MutableMap<String, String> = mutableMapOf()

    override fun validate(schemaName: String, value: JsonElement) {

        val schema = JsonSchema.fromDefinition(
            getSchema(schemaName),
        )

        val errors = mutableListOf<ValidationError>()

        val valid = schema.validate(value, errors::add)
        if (!valid) {
            throw ElementNotValidException(errors)
        }
    }

    private fun getSchema(schemaName: String): String {
        val loaded = loadedSchema[schemaName]
        if (loaded != null) {
            return loaded
        }

        val schemaFile = schemaMap[schemaName]
            ?: throw SchemaNotFoundException()

        val schema = ResourceUtils.getFile("classpath:json-schemas/$schemaFile")
            .readText(Charsets.UTF_8)
        loadedSchema[schemaName] = schema

        return schema
    }
}
