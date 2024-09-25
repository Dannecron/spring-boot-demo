package com.example.demo.responses

class BaseResponse(val status: ResponseStatus)

fun makeNotFound(): BaseResponse = BaseResponse(status = ResponseStatus.NOT_FOUND)