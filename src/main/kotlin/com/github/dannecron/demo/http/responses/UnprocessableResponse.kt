package com.github.dannecron.demo.http.responses

import org.springframework.validation.ObjectError

class UnprocessableResponse(
    val cause: String,
    val errors: List<ObjectError>?
): BaseResponse(status = ResponseStatus.UNPROCESSABLE) {
    constructor(cause: String): this(cause, null)
}
