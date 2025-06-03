package com.github.dannecron.demo.edgecontracts.api.exceptions

open class ApiException(
    override val message: String,
    override val cause: Throwable?,
) : RuntimeException(message, cause)
