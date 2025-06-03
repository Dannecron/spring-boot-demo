package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.view.CustomerExtended
import com.github.dannecron.demo.core.services.customer.CustomerService
import com.github.dannecron.demo.edgecontracts.api.CustomerApi
import com.github.dannecron.demo.edgecontracts.api.exceptions.NotFoundException
import com.github.dannecron.demo.edgecontracts.api.model.CityApiModel
import com.github.dannecron.demo.edgecontracts.api.model.CustomerApiModel
import com.github.dannecron.demo.edgecontracts.api.response.GetCustomerResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class CustomerController(
    private val customerService: CustomerService,
) : CustomerApi {

    @Throws(NotFoundException::class)
    override fun getCustomer(guid: UUID): ResponseEntity<GetCustomerResponse> {
        val customerExtended = customerService.findByGuid(guid) ?: throw NotFoundException(null)

        return ResponseEntity(customerExtended.toResponse(), HttpStatus.OK)
    }

    private fun CustomerExtended.toResponse() = GetCustomerResponse(
        customer = customer.toApiModel(),
        city = city?.toApiModel()
    )

    private fun Customer.toApiModel() = CustomerApiModel(
        id = id,
        guid = guid,
        name = name,
        cityId = cityId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun City.toApiModel() = CityApiModel(
        id = id,
        guid = guid,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
}
