package com.github.dannecron.demo.services.database.customer

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.Customer
import com.github.dannecron.demo.db.repository.CityRepository
import com.github.dannecron.demo.db.repository.CustomerRepository
import com.github.dannecron.demo.models.CustomerExtended
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val cityRepository: CityRepository,
    private val commonGenerator: CommonGenerator,
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
        guid = commonGenerator.generateUUID(),
        name = name,
        cityId = cityGuid?.let {
            cityRepository.findByGuid(it)?.id ?: throw CityNotFoundException()
        },
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
    ).let {
        customerRepository.save(it)
    }
}
