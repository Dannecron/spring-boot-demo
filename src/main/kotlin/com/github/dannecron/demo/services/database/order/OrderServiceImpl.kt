package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.models.Customer
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.models.order.Order
import com.github.dannecron.demo.models.order.OrderProduct
import com.github.dannecron.demo.models.order.OrderWithProducts
import com.github.dannecron.demo.providers.OrderProductRepository
import com.github.dannecron.demo.providers.OrderRepository
import com.github.dannecron.demo.providers.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.*

@Service
class OrderServiceImpl(
    @Autowired private val orderRepository: OrderRepository,
    @Autowired private val orderProductRepository: OrderProductRepository,
    @Autowired private val productRepository: ProductRepository,
) {
    fun getByCustomerId(customerId: Long): List<OrderWithProducts> = orderRepository.findByCustomerId(customerId)
        .let { orders -> orders.map { order -> OrderWithProducts(
            order = order,
            products = orderProductRepository.findByOrderId(orderId = order.id!!)
                .map { orderProduct -> orderProduct.productId }
                .let { productIds -> productRepository.findAllById(productIds).toList() }
        ) } }

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

        return orderRepository.save(order)
            .also {
                savedOrder -> products.toList()
                    .map { product -> OrderProduct(
                        guid = UUID.randomUUID(),
                        orderId = savedOrder.id!!,
                        productId = product.id!!,
                        createdAt = OffsetDateTime.now(),
                        updatedAt = null
                    ) }
                    .also { orderProductRepository.saveAll(it) }
            }
    }
}
