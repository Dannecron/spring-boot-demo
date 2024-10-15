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
    override fun findByGuid(guid: UUID): CustomerExtended? = customerRepository.findByGuid(guid)
        ?.let {
            customer -> CustomerExtended(
                customer = customer,
                city = customer.cityId?.let { cityId -> cityRepository.findById(cityId).orElse(null) }
            )
        }

    override fun create(name: String, cityGuid: UUID?): Customer = Customer(
        id = null,
        guid = UUID.randomUUID(),
        name = name,
        cityId = cityGuid?.let {
            cityRepository.findByGuid(it)?.id ?: throw CityNotFoundException()
        },
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
    ).let {
        customerRepository.save(it)
    }
}
