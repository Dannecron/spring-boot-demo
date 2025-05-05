package com.github.dannecron.demo.core.services.validation

import com.github.dannecron.demo.core.services.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.core.services.validation.exceptions.SchemaNotFoundException
import kotlinx.serialization.json.JsonElement

interface SchemaValidator {

    @Throws(ElementNotValidException::class, SchemaNotFoundException::class)
    fun validate(schemaName: String, value: JsonElement)
}
