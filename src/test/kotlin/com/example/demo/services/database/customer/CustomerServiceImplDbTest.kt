package com.example.demo.services.database.customer

import com.example.demo.BaseDbTest
import com.example.demo.models.City
import com.example.demo.providers.CityRepository
import com.example.demo.providers.CustomerRepository
import com.example.demo.services.database.exceptions.CityNotFoundException
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.*

@ContextConfiguration(classes = [CustomerRepository::class, CityRepository::class, CustomerServiceImpl::class])
class CustomerServiceImplDbTest: BaseDbTest() {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var cityRepository: CityRepository

    @Autowired
    private lateinit var customerServiceImpl: CustomerServiceImpl

    @Test
    fun createFind_success() {
        val nameOne = "Some Dude-One"
        val nameTwo = "Some Dude-Two"
        val nameThree = "Some Dude-Three"
        var city = City(
            id = null,
            guid = UUID.randomUUID(),
            name = "some city name",
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = null,
        )
        var customerIds = longArrayOf()

        try {
            city = cityRepository.save(city)

            customerServiceImpl.create(nameTwo, null).let {
                customerIds += it.id ?: fail("customerWithNoCity id is null")
                assertNull(it.cityId)
                assertNotNull(it.createdAt)
                assertNull(it.updatedAt)
            }

            val customerWithCity = customerServiceImpl.create(nameOne, city.guid)
            customerIds += customerWithCity.id ?: fail("customerWithCity id is null")
            assertEquals(city.id, customerWithCity.cityId)
            assertNotNull(customerWithCity.createdAt)
            assertNull(customerWithCity.updatedAt)

            val existedCustomer = customerServiceImpl.findByGuid(customerWithCity.guid)
            assertNotNull(existedCustomer)
            assertEquals(customerWithCity.id, existedCustomer.customer.id)
            assertEquals(city.id, existedCustomer.city?.id)

            assertThrows<CityNotFoundException> {
                customerServiceImpl.create(nameThree, UUID.randomUUID())
            }
        } finally {
            val cityId = city.id
            if (cityId != null) {
                cityRepository.deleteById(cityId)
            }

            customerIds.onEach { customerRepository.deleteById(it) }
        }
    }
}
