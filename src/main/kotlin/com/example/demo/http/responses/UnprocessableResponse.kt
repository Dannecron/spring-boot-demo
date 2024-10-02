package com.example.demo.http.responses

import org.springframework.validation.ObjectError

class UnprocessableResponse(
    val cause: String,
    val errors: List<ObjectError>? = null
): BaseResponse(status = ResponseStatus.UNPROCESSABLE)

fun makeUnprocessableResponse(cause: String): UnprocessableResponse = UnprocessableResponse(cause)
fun makeUnprocessableResponseWithErrors(
    cause: String, errors: List<ObjectError>,
): UnprocessableResponse = UnprocessableResponse(cause, errors)