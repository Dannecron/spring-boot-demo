package com.example.demo.controllers

import com.example.demo.exceptions.NotFoundException
import com.example.demo.provider.ProductRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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
}