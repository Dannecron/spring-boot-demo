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
import org.junit.jupiter.api.Test
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
    private val createdAt = OffsetDateTime.now()

    private val customerEntity = CustomerEntity(
        id = 1,
        guid = mockGuid,
        name = "name",
        cityId = cityId,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )
    private val customer = Customer(
        id = 1,
        guid = mockGuid,
        name = "name",
        cityId = cityId,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )

    private val cityEntity = CityEntity(
        id = cityId,
        guid = cityGuid,
        name = "city",
        createdAt = createdAt,
        updatedAt = null,
        deletedAt = null,
    )
    private val city = City(
        id = cityId,
        guid = cityGuid,
        name = "city",
        createdAt = createdAt,
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    fun `create - success - with city`() {
        whenever(customerRepository.save(any<CustomerEntity>())).thenReturn(customerEntity)
        whenever(cityRepository.findByGuid(cityGuid)).thenReturn(cityEntity)

        val result = customerServiceImpl.create("name", cityGuid)
        assertEquals(customer, result)

        verify(customerRepository, times(1)).save(customerEntity.copy(id = null))
        verify(cityRepository, times(1)).findByGuid(cityGuid)
    }

    @Test
    fun `create - success - no city`() {
        val customerNoCityEntity = customerEntity.copy(cityId = null)
        val customerNoCity = customer.copy(cityId = null)

        whenever(customerRepository.save(any<CustomerEntity>())).thenReturn(customerNoCityEntity)

        val result = customerServiceImpl.create("name", null)
        assertEquals(customerNoCity, result)

        verify(customerRepository, times(1)).save(customerNoCityEntity.copy(id = null))
        verifyNoInteractions(cityRepository)
    }

    @Test
    fun `create - fail - with city`() {
        whenever(customerRepository.save(any<CustomerEntity>())).thenReturn(customerEntity)
        whenever(cityRepository.findByGuid(cityGuid)).thenReturn(null)

        assertThrows<CityNotFoundException> {
            customerServiceImpl.create("name", cityGuid)
        }

        verify(customerRepository, never()).save(customerEntity.copy(id = null))
        verify(cityRepository, times(1)).findByGuid(cityGuid)
    }

    @Test
    fun `findByGuid - with city`() {
        val customerGuid = mockGuid
        whenever(customerRepository.findByGuid(any())).thenReturn(customerEntity)
        whenever(cityRepository.findById(any())).thenReturn(Optional.of(cityEntity))

        val result = customerServiceImpl.findByGuid(customerGuid)
        assertEquals(CustomerExtended(customer, city), result)

        verify(customerRepository, times(1)).findByGuid(customerGuid)
        verify(cityRepository, times(1)).findById(cityId)
    }

    @Test
    fun `findByGuid - no city`() {
        val customerGuid = mockGuid
        whenever(customerRepository.findByGuid(any())).thenReturn(customerEntity)
        whenever(cityRepository.findById(any())).thenReturn(Optional.empty())

        val result = customerServiceImpl.findByGuid(customerGuid)
        assertEquals(CustomerExtended(customer, null), result)

        verify(customerRepository, times(1)).findByGuid(customerGuid)
        verify(cityRepository, times(1)).findById(cityId)
    }
}
