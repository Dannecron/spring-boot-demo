package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.view.CustomerExtended
import com.github.dannecron.demo.core.services.customer.CustomerService
import com.github.dannecron.demo.edgecontracts.api.model.ResponseStatusModel
import com.github.dannecron.demo.edgerest.ExceptionHandler
import com.github.dannecron.demo.edgerest.WebTestConfig
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test

@WebMvcTest(CustomerController::class)
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = [
        WebTestConfig::class,
        CustomerController::class,
        ExceptionHandler::class,
    ]
)
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var customerService: CustomerService

    @Test
    fun getCustomer_successWithCity() {
        val customerId = 22L
        val cityId = 11L
        val customerGuid = UUID.randomUUID()
        val customerExtended = CustomerExtended(
            customer = Customer(
                id = customerId,
                guid = customerGuid,
                name = "Test Person",
                cityId = cityId,
                createdAt = OffsetDateTime.now().minusHours(1),
                updatedAt = OffsetDateTime.now(),
            ),
            city = City(
                id = cityId,
                guid = UUID.randomUUID(),
                name = "Test City",
                createdAt = OffsetDateTime.now().minusWeeks(1),
                updatedAt = null,
                deletedAt = null
            )
        )

        whenever(customerService.findByGuid(
            eq(customerGuid),
        )) doReturn customerExtended

        mockMvc.get("/api/customer/$customerGuid")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.customer.id") { value(customerId) } }
            .andExpect { jsonPath("\$.customer.cityId") { value(cityId) } }
            .andExpect { jsonPath("\$.city.id") { value(cityId) } }
    }

    @Test
    fun getCustomer_successNoCity() {
        val customerId = 22L
        val customerGuid = UUID.randomUUID()
        val customerExtended = CustomerExtended(
            customer = Customer(
                id = customerId,
                guid = customerGuid,
                name = "Test Person",
                cityId = null,
                createdAt = OffsetDateTime.now().minusHours(1),
                updatedAt = OffsetDateTime.now(),
            ),
            city = null,
        )

        whenever(customerService.findByGuid(
            eq(customerGuid),
        )) doReturn customerExtended

        mockMvc.get("/api/customer/$customerGuid")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.customer.id") { value(customerId) } }
            .andExpect { jsonPath("\$.customer.cityId") { value(null) } }
            .andExpect { jsonPath("\$.city") { value(null) } }
    }

    @Test
    fun getCustomer_successNotFound() {
        val customerGuid = UUID.randomUUID()

        whenever(customerService.findByGuid(
            eq(customerGuid),
        )) doReturn null

        mockMvc.get("/api/customer/$customerGuid")
            .andExpect { status { isNotFound() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.status") { value(ResponseStatusModel.NOT_FOUND.status) } }
    }
}
