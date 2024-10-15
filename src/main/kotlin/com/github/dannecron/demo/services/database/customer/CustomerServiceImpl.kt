package com.github.dannecron.demo.services.database.customer

import com.github.dannecron.demo.models.Customer
import com.github.dannecron.demo.models.CustomerExtended
import com.github.dannecron.demo.providers.CityRepository
import com.github.dannecron.demo.providers.CustomerRepository
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import java.time.OffsetDateTime
import java.util.*

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val cityRepository: CityRepository
): CustomerService {
    override fun findByGuid(guid: UUID): CustomerExtended? {
        val customer = customerRepository.findByGuid(guid) ?: return null

        if (customer.cityId == null) {
            return CustomerExtended(customer, null)
        }

        val city = cityRepository.findById(customer.cityId)

        return CustomerExtended(customer, city.orElse(null))
    }

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
