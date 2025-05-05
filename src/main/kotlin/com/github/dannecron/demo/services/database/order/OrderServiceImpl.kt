package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.Customer
import com.github.dannecron.demo.db.entity.Product
import com.github.dannecron.demo.db.entity.order.Order
import com.github.dannecron.demo.db.entity.order.OrderProduct
import com.github.dannecron.demo.db.repository.OrderProductRepository
import com.github.dannecron.demo.db.repository.OrderRepository
import com.github.dannecron.demo.db.repository.ProductRepository
import com.github.dannecron.demo.models.OrderWithProducts
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository,
    private val commonGenerator: CommonGenerator,
) {
    fun findByCustomerId(customerId: Long): List<OrderWithProducts> = orderRepository.findByCustomerId(customerId)
        .let { orders -> orders.map { order -> OrderWithProducts(
            order = order,
            products = findProductsByOrderId(order.id!!),
        ) } }

    @Transactional
    fun createOrder(customer: Customer, products: Set<Product>): Order {
        val order = Order(
            id = null,
            guid = commonGenerator.generateUUID(),
            customerId = customer.id!!,
            deliveredAt = null,
            createdAt = commonGenerator.generateCurrentTime(),
            updatedAt = null,
        )

        return orderRepository.save(order)
            .also { saveProductsForNewOrder(it, products.toList()) }
    }

    private fun findProductsByOrderId(orderId: Long): List<Product> =
        orderProductRepository.findByOrderId(orderId = orderId)
            .map { it.productId }
            .let {
                if (it.isEmpty()) {
                    emptyList()
                } else {
                    productRepository.findAllById(it).toList()
                }
            }

    private fun saveProductsForNewOrder(savedOrder: Order, products: List<Product>) {
        products.map {
            OrderProduct(
                guid = commonGenerator.generateUUID(),
                orderId = savedOrder.id!!,
                productId = it.id!!,
                createdAt = commonGenerator.generateCurrentTime(),
                updatedAt = null
            )
        }.also { orderProductRepository.saveAll(it) }
    }
}
