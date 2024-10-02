package com.example.demo.http.controllers

import com.example.demo.http.exceptions.NotFoundException
import com.example.demo.http.exceptions.UnprocessableException
import com.example.demo.http.requests.CreateProductRequest
import com.example.demo.http.responses.makeOkResponse
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.product.ProductService
import com.example.demo.services.database.product.exceptions.ProductNotFoundException
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import jakarta.validation.Valid
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
    @ResponseBody
    @Throws(NotFoundException::class)
    fun getProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<Any> {
        val product = productService.findByGuid(guid = guid) ?: throw NotFoundException()

        return ResponseEntity(product, HttpStatus.OK)
    }

    @PostMapping("/{guid}/sync")
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
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