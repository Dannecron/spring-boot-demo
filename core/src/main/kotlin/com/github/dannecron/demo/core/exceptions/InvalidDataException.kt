package com.github.dannecron.demo.core.exceptions

class InvalidDataException(
    override val message: String,
    override val cause: Throwable?,
) : RuntimeException(message, cause)
