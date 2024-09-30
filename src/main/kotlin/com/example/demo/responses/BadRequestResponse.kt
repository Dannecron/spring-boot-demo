package com.example.demo.responses

data class BadRequestResponse(
    val cause: String,
): BaseResponse(status = ResponseStatus.BAD_REQUEST)

fun makeBadRequestResponse(cause: String): BadRequestResponse = BadRequestResponse(cause)
