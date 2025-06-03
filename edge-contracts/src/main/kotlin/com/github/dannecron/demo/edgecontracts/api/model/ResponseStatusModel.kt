package com.github.dannecron.demo.edgecontracts.api.model

import com.fasterxml.jackson.annotation.JsonValue

enum class ResponseStatusModel(@JsonValue val status: String) {
    OK("ok"),
    NOT_FOUND("not found"),
    BAD_REQUEST("bad request"),
    UNPROCESSABLE("unprocessable"),
    INTERNAL_ERROR("internal");
}
