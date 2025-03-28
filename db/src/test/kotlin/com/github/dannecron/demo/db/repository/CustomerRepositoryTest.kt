package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.BaseDbTest
import com.github.dannecron.demo.db.entity.Customer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ContextConfiguration(classes = [CustomerRepository::class])
class CustomerRepositoryTest : BaseDbTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    private val customerGuid = UUID.fromString("823c50de-4c81-49bd-a69a-2d52be42b728")
    private val customer = Customer(
        id = 1000,
        guid = customerGuid,
        name = "Customer",
        cityId = 1000,
        createdAt = OffsetDateTime.parse("2025-01-01T12:10:05+00:00"),
        updatedAt = null,
    )

    @Test
    @Sql(scripts = ["/sql/insert_city.sql", "/sql/insert_customer.sql"])
    fun findByGuid() {
        val result = customerRepository.findByGuid(customerGuid)
        assertEquals(customer, result)

        val emptyResult = customerRepository.findByGuid(UUID.randomUUID())
        assertNull(emptyResult)
    }
}
