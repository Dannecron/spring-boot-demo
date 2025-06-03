package com.github.dannecron.demo.edgecontracts.validation

import com.github.dannecron.demo.edgecontracts.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.edgecontracts.validation.exceptions.SchemaNotFoundException
import kotlinx.serialization.json.JsonElement

interface SchemaValidator {

    @Throws(ElementNotValidException::class, SchemaNotFoundException::class)
    fun validate(schemaName: String, value: JsonElement)
}
