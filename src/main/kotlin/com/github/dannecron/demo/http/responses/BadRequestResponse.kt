package com.github.dannecron.demo.http.responses

data class BadRequestResponse(
    val cause: String,
): BaseResponse(status = ResponseStatus.BAD_REQUEST)
