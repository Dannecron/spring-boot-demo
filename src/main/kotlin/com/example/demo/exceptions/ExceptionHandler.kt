package com.example.demo.exceptions

import com.example.demo.responses.makeNotFoundResponse
import com.example.demo.responses.makeUnprocessableResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(): ResponseEntity<Any> = ResponseEntity(makeNotFoundResponse(), HttpStatus.NOT_FOUND)

    @ExceptionHandler(UnprocessableException::class)
    fun handleUnprocessable(exception: UnprocessableException): ResponseEntity<Any> = ResponseEntity(
        makeUnprocessableResponse(exception.message),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )
}