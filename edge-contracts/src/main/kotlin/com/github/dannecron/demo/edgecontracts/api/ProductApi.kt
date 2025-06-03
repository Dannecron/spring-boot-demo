package com.github.dannecron.demo.edgecontracts.api

import com.github.dannecron.demo.edgecontracts.api.exceptions.NotFoundException
import com.github.dannecron.demo.edgecontracts.api.exceptions.UnprocessableException
import com.github.dannecron.demo.edgecontracts.api.model.ProductApiModel
import com.github.dannecron.demo.edgecontracts.api.request.CreateProductRequest
import com.github.dannecron.demo.edgecontracts.api.response.common.BaseResponse
import com.github.dannecron.demo.edgecontracts.api.response.common.NotFoundResponse
import com.github.dannecron.demo.edgecontracts.api.response.page.PageResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@RequestMapping(value = ["/api/product"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface ProductApi {

    @GetMapping("")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = PageResponse::class)),
        ]),
    ])
    fun getProducts(
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<PageResponse<ProductApiModel>>

    @GetMapping("/{guid}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = ProductApiModel::class)),
        ]),
        ApiResponse(responseCode = "404", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = NotFoundResponse::class))
        ])
    ])
    @Throws(NotFoundException::class)
    fun getProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<ProductApiModel>

    @PostMapping(value = [""], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createProduct(
        @Valid @RequestBody product: CreateProductRequest,
    ): ResponseEntity<ProductApiModel>

    @DeleteMapping("/{guid}")
    @Throws(NotFoundException::class, UnprocessableException::class)
    fun deleteProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<BaseResponse>

    @PostMapping("/{guid}/send")
    @Throws(NotFoundException::class)
    fun sendProduct(
        @PathVariable guid: UUID,
        @RequestParam(required = false) topic: String?
    ): ResponseEntity<BaseResponse>
}
