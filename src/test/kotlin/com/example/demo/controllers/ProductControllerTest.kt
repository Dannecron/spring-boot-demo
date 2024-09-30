package com.example.demo.controllers

import com.example.demo.models.Product
import com.example.demo.responses.ResponseStatus
import com.example.demo.services.ProductService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.MethodArgumentNotValidException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@WebMvcTest(ProductController::class)
class ProductControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockBean
    private lateinit var productService: ProductService
    private val mapper = jacksonObjectMapper()


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
            deletedAt = null,
        )

        whenever(productService.findByGuid(
            eq(guid),
        )) doReturn product

        mockMvc.get("/api/product/$guid")
            .andExpect { status { status { isOk() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.id") { value(product.id.toString()) } }
            .andExpect { jsonPath("\$.guid") { value(guid.toString()) } }
            .andExpect { jsonPath("\$.name") { value("some") } }
            .andExpect { jsonPath("\$.createdAt") { value(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) } }
            .andExpect { jsonPath("\$.updatedAt") { value(nullValue()) } }
    }

    @Test
    fun getProduct_notFound() {
        val guid = UUID.randomUUID()

        whenever(productService.findByGuid(
            eq(guid),
        )) doReturn null

        mockMvc.get("/api/product/$guid")
            .andExpect { status { status { isNotFound() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.NOT_FOUND.status) } }
    }

    @Test
    fun createProduct_success() {
        val productId = 13.toLong()
        val name = "new-product"
        val description = null
        val price = 20000.toLong()

        val reqBody = mapper.writeValueAsString(
            mapOf("name" to name, "description" to description, "price" to price)
        )

        whenever(productService.create(
            eq(name),
            eq(price),
            eq(description)
        )) doReturn Product(
            id = productId,
            guid = UUID.randomUUID(),
            name = name,
            description = description,
            price = price,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = null,
        )

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { status { isCreated() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.id") { value(productId) } }
    }

    @Test
    fun createProduct_badRequest_noNameParam() {
        val description = null
        val price = 20000.toLong()

        val reqBody = mapper.writeValueAsString(
            mapOf("description" to description, "price" to price)
        )

        verifyNoInteractions(productService)

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { status { isBadRequest() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.BAD_REQUEST.status) } }
            .andExpect { jsonPath("\$.cause") { contains("name") } }
    }

    @Test
    fun createProduct_badRequest_emptyName() {
        val description = null
        val price = 20000.toLong()

        val reqBody = mapper.writeValueAsString(
            mapOf("name" to "", "description" to description, "price" to price)
        )

        verifyNoInteractions(productService)

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { status { isUnprocessableEntity() } } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.UNPROCESSABLE.status) } }
            .andExpect { jsonPath("\$.cause") { value(MethodArgumentNotValidException::class.qualifiedName) } }

    }
}