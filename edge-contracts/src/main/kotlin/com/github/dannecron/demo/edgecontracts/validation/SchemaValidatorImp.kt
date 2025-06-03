package com.github.dannecron.demo.edgecontracts.validation

import com.github.dannecron.demo.edgecontracts.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.edgecontracts.validation.exceptions.SchemaNotFoundException
import io.github.optimumcode.json.schema.JsonSchema
import io.github.optimumcode.json.schema.ValidationError
import kotlinx.serialization.json.JsonElement

class SchemaValidatorImp(
    private val schemaMap: Map<String, String>,
): SchemaValidator {

    @Throws(
        SchemaNotFoundException::class,
        ElementNotValidException::class,
    )
    override fun validate(schemaName: String, value: JsonElement) {
        JsonSchema.fromDefinition(
            getSchema(schemaName),
        ).also {
            val errors = mutableListOf<ValidationError>()

            if (!it.validate(value, errors::add)) {
                throw ElementNotValidException(errors)
            }
        }
    }

    @Throws(SchemaNotFoundException::class)
    private fun getSchema(schemaName: String) = schemaMap[schemaName]
        ?: throw SchemaNotFoundException()
}
