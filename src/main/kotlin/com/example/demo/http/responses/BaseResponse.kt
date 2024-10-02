package com.example.demo.http.responses

open class BaseResponse(val status: ResponseStatus)

fun makeOkResponse(): BaseResponse = BaseResponse(status = ResponseStatus.OK)

fun makeNotFoundResponse(): BaseResponse = BaseResponse(status = ResponseStatus.NOT_FOUND)