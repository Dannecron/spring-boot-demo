package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.BaseUnitTest
import com.github.dannecron.demo.http.responses.ResponseStatus
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.services.database.product.ProductService
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.MethodArgumentNotValidException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@WebMvcTest(ProductController::class)
class ProductControllerTest(@Autowired val mockMvc: MockMvc): BaseUnitTest() {
    @MockBean
    private lateinit var productService: ProductService

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
            .andExpect { status { isOk() } }
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
            .andExpect { status { isNotFound() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.NOT_FOUND.status) } }
    }

    @Test
    fun getProducts_success() {
        val now = OffsetDateTime.now()
        val pageRequest = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdAt"))

        whenever(productService.findAll(
            pageRequest,
        )) doReturn PageImpl(listOf(Product(
            id = 12,
            guid = UUID.randomUUID(),
            name = "some",
            description = null,
            price = 11130,
            createdAt = now,
            updatedAt = null,
            deletedAt = null,
        )))

        mockMvc.get("/api/product?page=1&size=2&sort=createdAt,desc")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.meta.total") { value(1) } }
            .andExpect { jsonPath("\$.meta.pages") { value(1) } }
            .andExpect { jsonPath("\$.data") { isArray() } }
            .andExpect { jsonPath("\$.data[0].id") { value(12) } }
            .andExpect { jsonPath("\$.data[0].name") { value("some") } }
            .andExpect { jsonPath("\$.data[0].description") { value(null) } }
            .andExpect { jsonPath("\$.data[0].createdAt") { value(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) } }
            .andExpect { jsonPath("\$.data[0].priceDouble") { value(111.30) } }
            .andExpect { jsonPath("\$.data[0].isDeleted") { value(false) } }
    }

    @Test
    fun createProduct_success() {
        val productId = 13L
        val name = "new-product"
        val description = null
        val price = 20000L

        val reqBody = """{"name":"$name","description":null,"price":$price}"""

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
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.id") { value(productId) } }
    }

    @Test
    fun createProduct_badRequest_noNameParam() {
        val price = 20000L

        val reqBody = """{"description":null,"price":$price}"""

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { isBadRequest() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.BAD_REQUEST.status) } }
            .andExpect { jsonPath("\$.cause") { contains("name") } }

        verifyNoInteractions(productService)
    }

    @Test
    fun createProduct_badRequest_emptyName() {
        val price = 20000L

        val reqBody = """{"name":"","description":null,"price":$price}"""

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { isUnprocessableEntity() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.UNPROCESSABLE.status) } }
            .andExpect { jsonPath("\$.cause") { value(MethodArgumentNotValidException::class.qualifiedName) } }

        verifyNoInteractions(productService)
    }

    @Test
    fun deleteProduct_success() {
        val guid = UUID.randomUUID()

        whenever(productService.delete(
            eq(guid),
        )) doReturn Product(
            id = 2133,
            guid = guid,
            name = "name",
            description = "description",
            price = 210202,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = OffsetDateTime.now(),
        )

        mockMvc.delete("/api/product/${guid}")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.OK.status) } }
    }
}
