package com.example.demo.models

data class Shop(val name: String, val customers: List<CustomerLocal>) {
    fun checkAllCustomersAreFrom(city: City): Boolean = customers.count { cus -> cus.city == city } == customers.count()

    fun countCustomersFrom(city: City): Int = customers.count { cus -> cus.city == city }

    fun getCitiesCustomersAreFrom(): Set<City> = customers.map { cus -> cus.city }.toSet()

    fun findAnyCustomerFrom(city: City): CustomerLocal? = customers.firstOrNull { cus -> cus.city == city }

    fun getAllOrderedProducts(): Set<Product> = customers.flatMap { cus -> cus.getOrderedProducts() }.toSet()

    fun getCustomersFrom(city: City): List<CustomerLocal> = customers.filter { cus -> cus.city == city }

    fun getCustomersSortedByNumberOfOrders(): List<CustomerLocal> = customers.sortedBy { cus -> cus.orders.count() }

    fun getCustomerWithMaximumNumberOfOrders(): CustomerLocal? = customers.maxByOrNull { cus -> cus.orders.count() }

    /**
     * Return customers who have more undelivered orders than delivered
     */
    fun getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<CustomerLocal> = customers.partition(predicate = fun (cus): Boolean {
        val (del, undel) = cus.orders.partition { ord -> ord.isDelivered }

        return del.count() < undel.count()
    }).first.toSet()

    fun getNumberOfTimesProductWasOrdered(product: Product): Int = customers
        .flatMap { cus -> cus.orders.flatMap { ord -> ord.products } }
        .count { pr -> pr.name == product.name }

    /**
     * Return the set of products that were ordered by every customer.
     * Note: a customer may order the same product for several times.
     */
    fun getSetOfProductsOrderedByEveryCustomer(): Set<Product> {
        val products = customers.flatMap { cus -> cus.orders.flatMap { ord -> ord.products } }.toSet()

        return customers.fold(products) { orderedProducts, cus ->
            orderedProducts.intersect(cus.orders.flatMap { ord -> ord.products }.toSet())
        }.toSet()
    }

    fun groupCustomersByCity(): Map<City, List<CustomerLocal>> = customers.groupBy { cus -> cus.city }

    fun hasCustomerFrom(city: City): Boolean = customers.any { cus -> cus.city == city }
}
