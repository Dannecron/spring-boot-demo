package com.example.demo.services.validation

import com.example.demo.services.validation.exceptions.ElementNotValidException
import com.example.demo.services.validation.exceptions.SchemaNotFoundException
import kotlinx.serialization.json.JsonElement

interface SchemaValidator {
    @Throws(ElementNotValidException::class, SchemaNotFoundException::class)
    fun validate(schemaName: String, value: JsonElement)
}