package com.github.dannecron.demo.edgecontracts.api.response.common

import com.github.dannecron.demo.edgecontracts.api.model.ResponseStatusModel

data class BadRequestResponse(
    val cause: String,
): BaseResponse(status = ResponseStatusModel.BAD_REQUEST)
