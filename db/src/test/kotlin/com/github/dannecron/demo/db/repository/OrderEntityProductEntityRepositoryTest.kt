package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.BaseDbTest
import com.github.dannecron.demo.db.entity.order.OrderProductEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@ContextConfiguration(classes = [OrderProductRepository::class])
class OrderEntityProductEntityRepositoryTest : BaseDbTest() {

    @Autowired
    private lateinit var orderProductRepository: OrderProductRepository
    
    private val orderId = 1000L
    private val orderProductEntity = OrderProductEntity(
        guid = UUID.fromString("930f54e2-c60d-448e-83b1-0d259ff2c2d3"),
        orderId = orderId,
        productId = 1000,
        createdAt = OffsetDateTime.parse("2025-01-01T12:10:05+00:00"),
        updatedAt = null,
    )

    @Test
    @Sql(
        scripts = [
            "/sql/insert_city.sql",
            "/sql/insert_customer.sql",
            "/sql/insert_order.sql",
            "/sql/insert_product.sql",
            "/sql/insert_order_product.sql",
        ]
    )
    fun findByOrderId() {
        val result = orderProductRepository.findByOrderId(orderId)
        assertEquals(1, result.size)
        assertEquals(orderProductEntity, result[0])
    }
}
