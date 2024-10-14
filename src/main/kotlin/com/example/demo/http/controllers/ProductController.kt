package com.example.demo.http.controllers

import com.example.demo.http.exceptions.NotFoundException
import com.example.demo.http.exceptions.UnprocessableException
import com.example.demo.http.requests.CreateProductRequest
import com.example.demo.http.responses.NotFoundResponse
import com.example.demo.http.responses.makeOkResponse
import com.example.demo.http.responses.page.PageResponse
import com.example.demo.models.Product
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.product.ProductService
import com.example.demo.services.database.exceptions.ProductNotFoundException
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/product"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ProductController(
    val productService: ProductService,
) {
    @GetMapping("/{guid}")
    @Throws(NotFoundException::class)
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = Product::class)),
        ]),
        ApiResponse(responseCode = "404", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = NotFoundResponse::class))
        ])
    ])
    fun getProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<Any> {
        val product = productService.findByGuid(guid = guid) ?: throw NotFoundException()

        return ResponseEntity(product, HttpStatus.OK)
    }

    @GetMapping("")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", content = [
            Content(mediaType = "application/json", schema = Schema(implementation = PageResponse::class)),
        ]),
    ])
    fun getProducts(
        @ParameterObject pageable: Pageable,
    ): ResponseEntity<Any> {
        val products = productService.findAll(pageable)

        return ResponseEntity(
            PageResponse(products),
            HttpStatus.OK,
        )
    }

    @PostMapping("/{guid}/sync")
    @Throws(NotFoundException::class)
    fun syncProductToKafka(
        @PathVariable guid: UUID,
        @RequestParam(required = false) topic: String?
    ): ResponseEntity<Any> {
        try {
            productService.syncToKafka(guid, topic)
        } catch (exception: InvalidArgumentException) {
            throw UnprocessableException("cannot sync product to kafka")
        }

        return ResponseEntity(makeOkResponse(), HttpStatus.OK)
    }

    @PostMapping(value = [""], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createProduct(
        @Valid @RequestBody product: CreateProductRequest,
    ): ResponseEntity<Any> {
        val saved = productService.create(
            product.name,
            product.price,
            product.description,
        )

        return ResponseEntity(saved, HttpStatus.CREATED)
    }

    @DeleteMapping("/{guid}")
    @Throws(NotFoundException::class, UnprocessableException::class)
    fun deleteProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<Any> {
        try {
            productService.delete(guid)
        } catch (notFoundException: ProductNotFoundException) {
            throw NotFoundException()
        } catch (alreadyDeletedException: AlreadyDeletedException) {
            throw UnprocessableException("product already deleted")
        }

        return ResponseEntity(makeOkResponse(), HttpStatus.OK)
    }
}
