package com.github.dannecron.demo.edgecontracts.api.exceptions

class UnprocessableException(
    override val message: String,
    override val cause: Throwable? = null,
): RuntimeException(message, cause)
