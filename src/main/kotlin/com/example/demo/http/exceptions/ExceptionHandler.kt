package com.example.demo.http.exceptions

import com.example.demo.http.responses.makeBadRequestResponse
import com.example.demo.http.responses.makeNotFoundResponse
import com.example.demo.http.responses.makeUnprocessableResponse
import com.example.demo.http.responses.makeUnprocessableResponseWithErrors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    /* 4xx status codes */

    // 400
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadable(exception: HttpMessageNotReadableException): ResponseEntity<Any> = ResponseEntity(
        makeBadRequestResponse(exception.message.toString()),
        HttpStatus.BAD_REQUEST,
    )

    // 404
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(): ResponseEntity<Any> = ResponseEntity(makeNotFoundResponse(), HttpStatus.NOT_FOUND)

    // 422
    @ExceptionHandler(UnprocessableException::class)
    fun handleUnprocessable(exception: UnprocessableException): ResponseEntity<Any> = ResponseEntity(
        makeUnprocessableResponse(exception.message),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ResponseEntity<Any> = ResponseEntity(
        makeUnprocessableResponseWithErrors(exception.javaClass.name, exception.allErrors),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )
}