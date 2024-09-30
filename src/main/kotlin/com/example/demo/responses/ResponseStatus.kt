package com.example.demo.responses

import com.fasterxml.jackson.annotation.JsonValue

enum class ResponseStatus(@JsonValue val status: String) {
    OK("ok"),
    NOT_FOUND("not found"),
    UNPROCESSABLE("unprocessable");
}