package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.order.OrderProductEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderProductRepository: CrudRepository<OrderProductEntity, UUID> {
    fun findByOrderId(orderId: Long): List<OrderProductEntity>
}
