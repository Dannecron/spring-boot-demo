package com.example.demo.controllers

import com.example.demo.exceptions.NotFoundException
import com.example.demo.exceptions.UnprocessableException
import com.example.demo.requests.CreateProductRequest
import com.example.demo.responses.makeOkResponse
import com.example.demo.services.ProductService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(value = ["/api/product"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ProductController(
    val productService: ProductService
) {
    @GetMapping("/{guid}")
    @ResponseBody
    @Throws(NotFoundException::class)
    fun getProduct(
        @PathVariable guid: UUID
    ): String {
        val product = productService.findByGuid(guid = guid) ?: throw NotFoundException()

        return Json.encodeToJsonElement(value = product).toString()
    }

    @PostMapping(value = ["/"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun createProduct(
        @RequestBody product: CreateProductRequest
    ): String {
        val saved = productService.create(
            product.name,
            product.price,
            product.description,
        )

        return Json.encodeToJsonElement(value = saved).toString()
    }

    @DeleteMapping("/{guid}")
    @ResponseBody
    @Throws(NotFoundException::class, UnprocessableException::class)
    fun deleteProduct(
        @PathVariable guid: UUID,
    ): ResponseEntity<Any> {
        productService.delete(guid)

        return ResponseEntity(makeOkResponse(), HttpStatus.OK)
    }
}