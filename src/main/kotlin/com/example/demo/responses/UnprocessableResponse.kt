package com.example.demo.responses

class UnprocessableResponse(
    val cause: String,
): BaseResponse(status = ResponseStatus.UNPROCESSABLE)

fun makeUnprocessableResponse(cause: String): UnprocessableResponse = UnprocessableResponse(cause)