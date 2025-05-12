package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.Order

interface OrderService {

    fun findByCustomerId(customerId: Long): List<Order>

    fun create(customer: Customer): Order
}
