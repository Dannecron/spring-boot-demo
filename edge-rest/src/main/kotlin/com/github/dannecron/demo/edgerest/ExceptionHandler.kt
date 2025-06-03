package com.github.dannecron.demo.edgerest

import com.github.dannecron.demo.core.utils.LoggerDelegate
import com.github.dannecron.demo.edgecontracts.api.exceptions.NotFoundException
import com.github.dannecron.demo.edgecontracts.api.exceptions.UnprocessableException
import com.github.dannecron.demo.edgecontracts.api.model.ResponseStatusModel
import com.github.dannecron.demo.edgecontracts.api.response.common.BadRequestResponse
import com.github.dannecron.demo.edgecontracts.api.response.common.BaseResponse
import com.github.dannecron.demo.edgecontracts.api.response.common.NotFoundResponse
import com.github.dannecron.demo.edgecontracts.api.response.common.UnprocessableResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {
    private val logger by LoggerDelegate()

    /* 4xx status codes */

    // 400
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMessageNotReadable(
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<BadRequestResponse> = ResponseEntity(
        BadRequestResponse(exception.message.toString()),
        HttpStatus.BAD_REQUEST,
    )

    // 404
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(): ResponseEntity<NotFoundResponse> = ResponseEntity(NotFoundResponse(), HttpStatus.NOT_FOUND)

    // 422
    @ExceptionHandler(UnprocessableException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleUnprocessable(exception: UnprocessableException): ResponseEntity<UnprocessableResponse> = ResponseEntity(
        UnprocessableResponse(exception.message),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<UnprocessableResponse> = ResponseEntity(
        UnprocessableResponse(exception.javaClass.name, exception.allErrors),
        HttpStatus.UNPROCESSABLE_ENTITY,
    )

    // 500
    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnexpectedRuntimeException(exception: RuntimeException): ResponseEntity<BaseResponse> = ResponseEntity(
        BaseResponse(ResponseStatusModel.INTERNAL_ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR,
    ).also {
        logger.error("internal server error", exception)
    }
}
