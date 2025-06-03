package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.BaseUnitTest
import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.services.product.ProductService
import com.github.dannecron.demo.http.responses.ResponseStatus
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
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
import java.util.UUID

@WebMvcTest(ProductController::class)
class ProductControllerTest: BaseUnitTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    private val guid = UUID.randomUUID()
    private val now = OffsetDateTime.now()
    private val productId = 12L
    private val productName = "new-product"
    private val productPrice = 20123L
    private val product = Product(
        id = productId,
        guid = guid,
        name = productName,
        description = null,
        price = productPrice,
        createdAt = now,
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    fun `getProduct - 200`() {
        whenever(productService.findByGuid(any())).thenReturn(product)

        mockMvc.get("/api/product/$guid")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.id") { value(product.id.toString()) } }
            .andExpect { jsonPath("\$.guid") { value(guid.toString()) } }
            .andExpect { jsonPath("\$.name") { value(productName) } }
            .andExpect { jsonPath("\$.createdAt") { value(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) } }
            .andExpect { jsonPath("\$.updatedAt") { value(nullValue()) } }

        verify(productService, times(1)).findByGuid(guid)
    }

    @Test
    fun `getProduct - 404`() {
        whenever(productService.findByGuid(any())).thenReturn(null)

        mockMvc.get("/api/product/$guid")
            .andExpect { status { isNotFound() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.NOT_FOUND.status) } }

        verify(productService, times(1)).findByGuid(guid)
    }

    @Test
    fun `getProducts - 200`() {
        val pageRequest = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdAt"))

        whenever(productService.findAll(any()))
            .thenReturn(PageImpl(listOf(product)))

        mockMvc.get("/api/product?page=1&size=2&sort=createdAt,desc")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.meta.total") { value(1) } }
            .andExpect { jsonPath("\$.meta.pages") { value(1) } }
            .andExpect { jsonPath("\$.data") { isArray() } }
            .andExpect { jsonPath("\$.data[0].id") { value(productId) } }
            .andExpect { jsonPath("\$.data[0].name") { value(productName) } }
            .andExpect { jsonPath("\$.data[0].description") { value(null) } }
            .andExpect { jsonPath("\$.data[0].createdAt") { value(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) } }
            .andExpect { jsonPath("\$.data[0].priceDouble") { value(201.23) } }
            .andExpect { jsonPath("\$.data[0].isDeleted") { value(false) } }

        verify(productService, times(1)).findAll(pageRequest)
    }

    @Test
    fun `createProduct - 200`() {
        val reqBody = """{"name":"$productName","description":null,"price":$productPrice}"""

        whenever(productService.create(any(), any(), anyOrNull())).thenReturn(product)

        mockMvc.post("/api/product") {
            contentType = MediaType.APPLICATION_JSON
            content = reqBody
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.id") { value(productId) } }

        verify(productService, times(1)).create(productName, productPrice, null)
    }

    @Test
    fun `createProduct - 400 - no name param`() {
        val reqBody = """{"description":null,"price":$productPrice}"""

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
    fun `createProduct - 400 - empty name param`() {
        val reqBody = """{"name":"","description":null,"price":$productPrice}"""

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
    fun `deleteProduct - 200`() {
        val deletedProduct = product.copy(deletedAt = now)

        whenever(productService.delete(any())).thenReturn(deletedProduct)

        mockMvc.delete("/api/product/${guid}")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatus.OK.status) } }

        verify(productService, times(1)).delete(guid)
    }
}
