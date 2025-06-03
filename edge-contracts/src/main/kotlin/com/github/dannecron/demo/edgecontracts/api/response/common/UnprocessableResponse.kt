package com.github.dannecron.demo.edgecontracts.api.response.common

import com.github.dannecron.demo.edgecontracts.api.model.ResponseStatusModel
import org.springframework.validation.ObjectError

class UnprocessableResponse(
    val cause: String,
    val errors: List<ObjectError>?
): BaseResponse(status = ResponseStatusModel.UNPROCESSABLE) {
    constructor(cause: String): this(cause, null)
}
