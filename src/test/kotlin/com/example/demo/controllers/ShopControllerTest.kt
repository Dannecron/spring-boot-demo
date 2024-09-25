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
import kotlin.test.Test

@WebMvcTest(ShopController::class)
class ShopControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var shopProvider: ShopProvider

    @Test
    fun commonInfo_shouldSeeSuccessResponseJson() {
        val shopMock = Shop(name="shop", customers= listOf(
            Customer(
                name = "cus-one",
                city = City(name= "city-one"),
                orders = listOf(
                    Order(products = listOf(Product(name = "one", price = 11.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "two", price = 13.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "three", price = 15.2)), isDelivered = true),
                )
            ),
            Customer(
                name = "cus-two",
                city = City(name= "city-two"),
                orders = listOf(
                    Order(products = listOf(Product(name = "one", price = 12.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "two", price = 13.2)), isDelivered = true),
                    Order(products = listOf(Product(name = "four", price = 14.2)), isDelivered = true),
                )
            ),
        ))

        val expectedJson: String = """{
            |"customers": {"withMoreUndeliveredOrdersThanDelivered": ["cus-one"]},
            |"products": {"orderedByAllCustomers": ["two"]}
            |}""".trimMargin()

        whenever(
            shopProvider.getRandomShop()
        ) doReturn (shopMock)

        mockMvc.perform(get("/shop/common-info"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json(expectedJson))
    }
}