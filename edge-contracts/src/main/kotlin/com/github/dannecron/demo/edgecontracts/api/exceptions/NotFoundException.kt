package com.github.dannecron.demo.edgecontracts.api.exceptions

class NotFoundException(
    override val cause: Throwable?,
): ApiException(
    message = "Not found",
    cause = cause,
)
