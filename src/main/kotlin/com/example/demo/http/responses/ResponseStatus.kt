package com.example.demo.http.responses

import com.fasterxml.jackson.annotation.JsonValue

enum class ResponseStatus(@JsonValue val status: String) {
    OK("ok"),
    NOT_FOUND("not found"),
    BAD_REQUEST("bad request"),
    UNPROCESSABLE("unprocessable");
}