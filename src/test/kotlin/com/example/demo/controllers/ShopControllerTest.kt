package com.example.demo.controllers

import com.example.demo.models.*
import com.example.demo.provider.ShopProvider
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test

@WebMvcTest(ShopController::class)
class ShopControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var shopProvider: ShopProvider

    @Test
    fun commonInfo_shouldSeeSuccessResponseJson() {
        val productOne = makeProduct(id = 1, name = "one", price = 11.2)
        val productTwo = makeProduct(id = 2, name = "two", price = 13.2)
        val productThree = makeProduct(id = 3, name = "three", price = 15.2)
        val productFour = makeProduct(id = 4, name = "four", price = 14.2)

        val shopMock = Shop(name="shop", customers= listOf(
            Customer(
                name = "cus-one",
                city = City(name= "city-one"),
                orders = listOf(
                    Order(products = listOf(productOne), isDelivered = false),
                    Order(products = listOf(productTwo), isDelivered = false),
                    Order(products = listOf(productThree), isDelivered = true),
                )
            ),
            Customer(
                name = "cus-two",
                city = City(name= "city-two"),
                orders = listOf(
                    Order(products = listOf(productOne), isDelivered = false),
                    Order(products = listOf(productTwo), isDelivered = true),
                    Order(products = listOf(productFour), isDelivered = true),
                )
            ),
        ))

        val expectedJson: String = """{
            |"customers": {"withMoreUndeliveredOrdersThanDelivered": ["cus-one"]},
            |"products": {"orderedByAllCustomers": ["one", "two"]}
            |}""".trimMargin()

        whenever(
            shopProvider.getRandomShop()
        ) doReturn (shopMock)

        mockMvc.perform(get("/shop/common-info"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json(expectedJson))
    }

    @Test
    fun commonInfo_shouldSeeNotFoundResponse() {
        whenever(
            shopProvider.getRandomShop()
        ) doReturn (null)

        mockMvc.perform(get("/shop/common-info"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json("""{"status":"not found"}"""))
    }

    private fun makeProduct(id: Long, name: String, price: Double): Product {
        return Product(
            id = id,
            guid = UUID.randomUUID(),
            name = name,
            description = null,
            price = (price * 100).toLong(),
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = null,
        )
    }
}