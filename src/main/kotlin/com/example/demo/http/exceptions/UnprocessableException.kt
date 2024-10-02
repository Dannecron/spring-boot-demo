package com.example.demo.http.exceptions

class UnprocessableException(override val message: String): RuntimeException(message)