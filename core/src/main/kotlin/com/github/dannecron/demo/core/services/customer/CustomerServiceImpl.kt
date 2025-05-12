package com.github.dannecron.demo.core.services.customer

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.view.CustomerExtended
import com.github.dannecron.demo.core.exceptions.CityNotFoundException
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.CityEntity
import com.github.dannecron.demo.db.entity.CustomerEntity
import com.github.dannecron.demo.db.repository.CityRepository
import com.github.dannecron.demo.db.repository.CustomerRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val cityRepository: CityRepository,
    private val commonGenerator: CommonGenerator,
) : CustomerService {
    override fun findByGuid(guid: UUID): CustomerExtended? = customerRepository.findByGuid(guid)
        ?.let { customer ->
            CustomerExtended(
                customer = customer.toCore(),
                city = customer.cityId?.let { cityId -> cityRepository.findById(cityId).getOrNull()?.toCore() }
            )
        }

    @Throws(CityNotFoundException::class)
    override fun create(name: String, cityGuid: UUID?): Customer = CustomerEntity(
        id = null,
        guid = commonGenerator.generateUUID(),
        name = name,
        cityId = cityGuid?.let {
            cityRepository.findByGuid(it)?.id ?: throw CityNotFoundException()
        },
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
    ).let(customerRepository::save)
        .toCore()
    
    private fun CustomerEntity.toCore() = Customer(
        id = id!!,
        guid = guid,
        name = name,
        cityId = cityId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    private fun CityEntity.toCore() = City(
        id = id!!,
        guid = guid,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
}
