package com.github.dannecron.demo.http.responses

open class BaseResponse(val status: ResponseStatus)

fun makeOkResponse(): BaseResponse = BaseResponse(status = ResponseStatus.OK)
