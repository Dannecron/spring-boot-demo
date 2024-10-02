package com.example.demo.http.controllers

import com.example.demo.http.exceptions.NotFoundException
import com.example.demo.providers.ShopProvider
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShopController (
    @field:Autowired private val shopProvider: ShopProvider,
) {
    @GetMapping(value = ["/shop/common-info"], produces = ["application/json"])
    @ResponseBody
    @Throws(NotFoundException::class)
    fun commonInfo(response: HttpServletResponse): String {
        val shop = shopProvider.getRandomShop() ?: throw NotFoundException()

        return Json.encodeToJsonElement(value = mapOf(
            "customers" to mapOf(
                "withMoreUndeliveredOrdersThanDelivered" to shop.getCustomersWithMoreUndeliveredOrdersThanDelivered().map { cus -> cus.name }
            ),
            "products" to mapOf(
                "orderedByAllCustomers" to shop.getSetOfProductsOrderedByEveryCustomer().map { pr -> pr.name },
            )
        )).toString()
    }
}