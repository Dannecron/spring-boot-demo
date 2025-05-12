package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.Order
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.order.OrderEntity
import com.github.dannecron.demo.db.repository.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val commonGenerator: CommonGenerator,
) : OrderService {
    override fun findByCustomerId(customerId: Long): List<Order> =
        orderRepository.findByCustomerId(customerId).map { it.toCore() }

    override fun create(customer: Customer): Order = OrderEntity(
        id = null,
        guid = commonGenerator.generateUUID(),
        customerId = customer.id,
        deliveredAt = null,
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
    ).let(orderRepository::save)
        .toCore()

    private fun OrderEntity.toCore() = Order(
        id = id!!,
        guid = guid,
        customerId = customerId,
        deliveredAt = deliveredAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
