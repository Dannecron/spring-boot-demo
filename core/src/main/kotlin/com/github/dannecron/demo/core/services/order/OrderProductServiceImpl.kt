package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Order
import com.github.dannecron.demo.core.dto.OrderProduct
import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.order.OrderProductEntity
import com.github.dannecron.demo.db.repository.OrderProductRepository
import org.springframework.stereotype.Service

@Service
class OrderProductServiceImpl(
    private val orderProductRepository: OrderProductRepository,
    private val commonGenerator: CommonGenerator
) : OrderProductService {
    override fun create(order: Order, product: Product): OrderProduct = OrderProductEntity(
        guid = commonGenerator.generateUUID(),
        orderId = order.id,
        productId = product.id,
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
    ).let(orderProductRepository::save)
        .toCore()

    private fun OrderProductEntity.toCore() = OrderProduct(
        guid = guid,
        orderId = orderId,
        productId = productId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
