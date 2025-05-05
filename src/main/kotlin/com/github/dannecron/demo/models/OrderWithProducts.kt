package com.github.dannecron.demo.models

import com.github.dannecron.demo.db.entity.Product
import com.github.dannecron.demo.db.entity.order.Order

data class OrderWithProducts(
    val order: Order,
    val products: List<Product>,
) {
    fun getMostExpensiveOrderedProduct(): Product? = products.maxByOrNull { pr -> pr.price }

    fun getTotalOrderPrice(): Double = products.sumOf { pr -> pr.getPriceDouble() }
}
