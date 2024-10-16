package com.github.dannecron.demo.models.order

import com.github.dannecron.demo.models.Product

data class OrderWithProducts(
    val order: Order,
    val products: List<Product>,
) {
    fun getMostExpensiveOrderedProduct(): Product? = products.maxByOrNull { pr -> pr.price }

    fun getTotalOrderPrice(): Double = products.sumOf { pr -> pr.getPriceDouble() }
}
