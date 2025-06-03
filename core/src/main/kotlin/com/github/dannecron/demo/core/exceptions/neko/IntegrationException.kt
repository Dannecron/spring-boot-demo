package com.github.dannecron.demo.core.exceptions.neko

class IntegrationException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException()
