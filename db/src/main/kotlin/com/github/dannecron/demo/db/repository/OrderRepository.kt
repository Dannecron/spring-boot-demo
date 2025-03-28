package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.order.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CrudRepository<Order, Long> {
    fun findByCustomerId(customerId: Long): List<Order>
}
