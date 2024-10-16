package com.github.dannecron.demo.models

data class CustomerLocal(val name: String, val city: City, val orders: List<OrderLocal>) {
    /**
     * Return the most expensive product among all delivered products
     */
    fun getMostExpensiveDeliveredProduct(): Product? = orders.filter { ord -> ord.isDelivered }
        .flatMap { ord -> ord.products }
        .maxByOrNull { pr -> pr.price }

    fun getMostExpensiveOrderedProduct(): Product? = orders.flatMap { ord -> ord.products }.maxByOrNull { pr -> pr.price }

    fun getOrderedProducts(): Set<Product> = orders.flatMap { order -> order.products }.toSet()

    fun getTotalOrderPrice(): Double = orders.flatMap { ord -> ord.products }.sumOf { pr -> pr.getPriceDouble()}
}
