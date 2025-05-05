package com.github.dannecron.demo.services.database.customer

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.City
import com.github.dannecron.demo.db.entity.Customer
import com.github.dannecron.demo.db.repository.CityRepository
import com.github.dannecron.demo.db.repository.CustomerRepository
import com.github.dannecron.demo.models.CustomerExtended
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomerServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val customerRepository: CustomerRepository = mock()
    private val cityRepository: CityRepository = mock()
    private val customerServiceImpl = CustomerServiceImpl(
        customerRepository = customerRepository,
        cityRepository = cityRepository,
        commonGenerator = commonGenerator,
    )

    private val cityId = 123L
    private val cityGuid = UUID.randomUUID()
    private val customer = Customer(
        id = 1,
        guid = mockGuid,
        name = "name",
        cityId = cityId,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )
    private val city = City(
        id = cityId,
        guid = cityGuid,
        name = "city",
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    fun `create - success - with city`() {
        whenever(customerRepository.save(any<Customer>())).thenReturn(customer)
        whenever(cityRepository.findByGuid(cityGuid)).thenReturn(city)

        val result = customerServiceImpl.create("name", cityGuid)
        assertEquals(customer, result)

        verify(customerRepository, times(1)).save(customer.copy(id = null))
        verify(cityRepository, times(1)).findByGuid(cityGuid)
    }

    @Test
    fun `create - success - no city`() {
        val customerNoCity = customer.copy(cityId = null)

        whenever(customerRepository.save(any<Customer>())).thenReturn(customerNoCity)

        val result = customerServiceImpl.create("name", null)
        assertEquals(customerNoCity, result)

        verify(customerRepository, times(1)).save(customerNoCity.copy(id = null))
        verifyNoInteractions(cityRepository)
    }

    @Test
    fun `create - fail - with city`() {
        whenever(customerRepository.save(any<Customer>())).thenReturn(customer)
        whenever(cityRepository.findByGuid(cityGuid)).thenReturn(null)

        assertThrows<CityNotFoundException> {
            customerServiceImpl.create("name", cityGuid)
        }

        verify(customerRepository, never()).save(customer.copy(id = null))
        verify(cityRepository, times(1)).findByGuid(cityGuid)
    }

    @Test
    fun `findByGuid - with city`() {
        val customerGuid = mockGuid
        whenever(customerRepository.findByGuid(any())).thenReturn(customer)
        whenever(cityRepository.findById(any())).thenReturn(Optional.of(city))

        val result = customerServiceImpl.findByGuid(customerGuid)
        assertEquals(CustomerExtended(customer, city), result)

        verify(customerRepository, times(1)).findByGuid(customerGuid)
        verify(cityRepository, times(1)).findById(cityId)
    }

    @Test
    fun `findByGuid - no city`() {
        val customerGuid = mockGuid
        whenever(customerRepository.findByGuid(any())).thenReturn(customer)
        whenever(cityRepository.findById(any())).thenReturn(Optional.empty())

        val result = customerServiceImpl.findByGuid(customerGuid)
        assertEquals(CustomerExtended(customer, null), result)

        verify(customerRepository, times(1)).findByGuid(customerGuid)
        verify(cityRepository, times(1)).findById(cityId)
    }
}
