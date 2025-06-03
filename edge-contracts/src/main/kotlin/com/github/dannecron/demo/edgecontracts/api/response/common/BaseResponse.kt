package com.github.dannecron.demo.edgecontracts.api.response.common

import com.github.dannecron.demo.edgecontracts.api.model.ResponseStatusModel

open class BaseResponse(val status: ResponseStatusModel)

fun makeOkResponse(): BaseResponse = BaseResponse(status = ResponseStatusModel.OK)
