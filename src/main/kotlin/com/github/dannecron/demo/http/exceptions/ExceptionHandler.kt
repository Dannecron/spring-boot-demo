package com.github.dannecron.demo.http.exceptions

import com.github.dannecron.demo.http.responses.BadRequestResponse
import com.github.dannecron.demo.http.responses.BaseResponse
import com.github.dannecron.demo.http.responses.NotFoundResponse
import com.github.dannecron.demo.http.responses.UnprocessableResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {
    /* 4xx status codes */

    // 400
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMessageNotReadable(exception: HttpMessageNotReadableException): ResponseEntity<Any> = ResponseEntity(
        BadRequestResponse(exception.message.toString()),
        HttpStatus.BAD_REQUEST,
    )

    // 404
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(): ResponseEntity<Any> = ResponseEntity(NotFoundResponse(), HttpStatus.NOT_FOUND)

    // 422
    @ExceptionHandler(UnprocessableException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleUnprocessable(exception: UnprocessableException): ResponseEntity<Any> = ResponseEntity(
        UnprocessableResponse(exception.message),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ResponseEntity<Any> = ResponseEntity(
        UnprocessableResponse(exception.javaClass.name, exception.allErrors),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )

    // 500
    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnexpectedRuntimeException(exception: RuntimeException): ResponseEntity<Any> = ResponseEntity(
        BaseResponse(com.github.dannecron.demo.http.responses.ResponseStatus.INTERNAL_ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR,
    )
}
