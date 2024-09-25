package com.example.demo.exceptions

import com.example.demo.responses.makeNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(): ResponseEntity<Any> = ResponseEntity(makeNotFound(), HttpStatus.NOT_FOUND)
}