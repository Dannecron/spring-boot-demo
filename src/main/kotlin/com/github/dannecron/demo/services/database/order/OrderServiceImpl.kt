package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.models.Customer
import com.github.dannecron.demo.models.Order
import com.github.dannecron.demo.models.OrderProduct
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.providers.OrderProductRepository
import com.github.dannecron.demo.providers.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.*

@Service
class OrderServiceImpl(
    @Autowired val orderRepository: OrderRepository,
    @Autowired val orderProductRepository: OrderProductRepository,
) {
    @Transactional
    fun createOrder(customer: Customer, products: Set<Product>): Order {
        val order = Order(
            id = null,
            guid = UUID.randomUUID(),
            customerId = customer.id!!,
            deliveredAt = null,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
        )
        return orderRepository.save(order).also {
            savedOrder -> products.toList().forEach {
                product -> orderProductRepository.save(OrderProduct(
                    guid = UUID.randomUUID(),
                    orderId = savedOrder.id!!,
                    productId = product.id!!,
                    createdAt = OffsetDateTime.now(),
                    updatedAt = null
                ))
            }
        }
    }
}
