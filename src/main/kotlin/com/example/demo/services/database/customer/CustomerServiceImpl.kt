package com.example.demo.services.database.customer

import com.example.demo.models.Customer
import com.example.demo.providers.CityRepository
import com.example.demo.providers.CustomerRepository
import com.example.demo.services.database.exceptions.CityNotFoundException
import java.time.OffsetDateTime
import java.util.*

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val cityRepository: CityRepository
): CustomerService {
    override fun findByGuid(guid: UUID): Customer? = customerRepository.findByGuid(guid)

    override fun create(name: String, cityGuid: UUID?): Customer {
        val cityId: Long? = cityGuid?.let {
            cityRepository.findByGuid(it)?.id ?: throw CityNotFoundException()
        }

        val customer = Customer(
            id = null,
            guid = UUID.randomUUID(),
            name = name,
            cityId = cityId,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
        )

        return customerRepository.save(customer)
    }
}
