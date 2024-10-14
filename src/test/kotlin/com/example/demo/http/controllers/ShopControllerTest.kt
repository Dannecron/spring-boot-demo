package com.example.demo.http.controllers

import com.example.demo.BaseUnitTest
import com.example.demo.models.*
import com.example.demo.providers.ShopProvider
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test

@WebMvcTest(ShopController::class)
class ShopControllerTest(@Autowired val mockMvc: MockMvc): BaseUnitTest() {
    @MockBean
    private lateinit var shopProvider: ShopProvider

    @Test
    fun commonInfo_shouldSeeSuccessResponseJson() {
        val productOne = makeProduct(id = 1, name = "one", price = 11.2)
        val productTwo = makeProduct(id = 2, name = "two", price = 13.2)
        val productThree = makeProduct(id = 3, name = "three", price = 15.2)
        val productFour = makeProduct(id = 4, name = "four", price = 14.2)

        val shopMock = Shop(name="shop", customers= listOf(
            CustomerLocal(
                name = "cus-one",
                city = makeCity(id = 1, name = "city-one"),
                orders = listOf(
                    Order(products = listOf(productOne), isDelivered = false),
                    Order(products = listOf(productTwo), isDelivered = false),
                    Order(products = listOf(productThree), isDelivered = true),
                )
            ),
            CustomerLocal(
                name = "cus-two",
                city = makeCity(id = 2, name = "city-two"),
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
        ) doReturn shopMock

        mockMvc.get("/shop/common-info")
            .andExpect{ status { status { isOk() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { content { json(expectedJson) } }
    }

    @Test
    fun commonInfo_shouldSeeNotFoundResponse() {
        whenever(
            shopProvider.getRandomShop()
        ) doReturn null

        mockMvc.get("/shop/common-info")
            .andExpect { status { isNotFound() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { content { json("""{"status":"not found"}""") } }
    }

    private fun makeProduct(id: Long, name: String, price: Double): Product = Product(
        id = id,
        guid = UUID.randomUUID(),
        name = name,
        description = null,
        price = (price * 100).toLong(),
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )

    private fun makeCity(id: Long, name: String): City = City(
        id = id,
        guid = UUID.randomUUID(),
        name = name,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )
}
