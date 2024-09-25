package com.example.demo.controllers

import com.example.demo.models.Product
import com.example.demo.provider.ProductRepository
import com.example.demo.responses.ResponseStatus
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@WebMvcTest(ProductController::class)
class ProductControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var productRepository: ProductRepository

    @Test
    fun getProduct_success() {
        val guid = UUID.randomUUID()
        val now = OffsetDateTime.now()
        val product = Product(
            id = 12,
            guid = guid,
            name = "some",
            description = null,
            price = 11130,
            createdAt = now,
            updatedAt = null,
        )

        whenever(productRepository.findByGuid(
            eq(guid),
        )) doReturn product

        mockMvc.get("/api/product/$guid")
            .andExpect { status { status { isOk() } } }
            .andExpect { content { contentType("application/json") } }
            .andExpect { jsonPath("\$.id") { value(product.id.toString()) } }
            .andExpect { jsonPath("\$.guid") { value(guid.toString()) } }
            .andExpect { jsonPath("\$.name") { value("some") } }
            .andExpect { jsonPath("\$.createdAt") { value(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) } }
            .andExpect { jsonPath("\$.updatedAt") { value(nullValue()) } }
    }

    @Test
    fun getProduct_notFound() {
        val guid = UUID.randomUUID()

        whenever(productRepository.findByGuid(
            eq(guid),
        )) doReturn null

        mockMvc.get("/api/product/$guid")
            .andExpect { status { status { isNotFound() } } }
            .andExpect { content { contentType("application/json") } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.NOT_FOUND.status) } }
    }
}