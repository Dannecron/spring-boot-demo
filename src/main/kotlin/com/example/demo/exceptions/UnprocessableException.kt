package com.example.demo.exceptions

class UnprocessableException(override val message: String): RuntimeException(message)