package com.example.demo.controllers

import com.example.demo.provider.ShopProvider
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShopController (
    @field:Autowired private val shopProvider: ShopProvider,
) {
    @GetMapping(value = ["/shop/common-info"], produces = ["application/json"] )
    @ResponseBody
    fun commonInfo(response: HttpServletResponse): String {
        response.contentType = "application/json"

        val shop = shopProvider.getRandomShop()

        if (shop == null) {
            response.status = HttpStatus.NOT_FOUND.value()

            return "not found"
        }

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