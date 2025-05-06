package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.order.OrderEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CrudRepository<OrderEntity, Long> {
    fun findByCustomerId(customerId: Long): List<OrderEntity>
}
