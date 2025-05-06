package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.BaseDbTest
import com.github.dannecron.demo.db.entity.order.OrderEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@ContextConfiguration(classes = [OrderRepository::class])
class OrderEntityRepositoryTest : BaseDbTest() {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    private val orderGuid = UUID.fromString("2c960a08-7187-4e91-9ef3-275c91b1342c")
    private val customerId = 1000L
    private val orderEntity = OrderEntity(
        id = 1000,
        guid = orderGuid,
        customerId = customerId,
        deliveredAt = null,
        createdAt = OffsetDateTime.parse("2025-01-01T12:10:05+00:00"),
        updatedAt = null,
    )

    @Test
    @Sql(
        scripts = [
            "/sql/insert_city.sql",
            "/sql/insert_customer.sql",
            "/sql/insert_order.sql",
        ]
    )
    fun findByGuid() {
        val result = orderRepository.findByCustomerId(customerId)
        assertEquals(1, result.size)
        assertEquals(orderEntity, result[0])
    }
}
