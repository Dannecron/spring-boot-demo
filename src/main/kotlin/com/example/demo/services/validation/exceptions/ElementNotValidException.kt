package com.example.demo.services.validation.exceptions

import io.github.optimumcode.json.schema.ValidationError

class ElementNotValidException(
    val validationErrors: List<ValidationError>,
): RuntimeException()