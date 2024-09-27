package com.example.demo.controllers

import com.example.demo.exceptions.NotFoundException
import com.example.demo.models.Product
import com.example.demo.provider.ProductRepository
import com.example.demo.requests.CreateProductRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import java.util.*

@RestController
@RequestMapping(value = ["/api/product"])
class ProductController(
    @Autowired val productRepository: ProductRepository
) {
    @GetMapping(value = ["{guid}"], produces = ["application/json"])
    @ResponseBody
    fun getProduct(
        @PathVariable guid: UUID
    ): String {
        val product = productRepository.findByGuid(guid = guid) ?: throw NotFoundException()

        return Json.encodeToJsonElement(value = product).toString()
    }

    @PostMapping(value = [""], consumes = ["application/json"], produces = ["application/json"])
    @ResponseBody
    fun createProduct(
        @RequestBody product: CreateProductRequest
    ): String {
        val productModel = Product(
            id = null,
            guid = UUID.randomUUID(),
            name = product.name,
            description = product.description,
            price = product.price,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
        )

        val saved = productRepository.save(productModel)

        return Json.encodeToJsonElement(value = saved).toString()
    }

    // todo delete with soft-delete
}