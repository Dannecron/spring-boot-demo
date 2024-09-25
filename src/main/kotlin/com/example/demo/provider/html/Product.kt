package com.example.demo.provider.html

data class Product(val description: String, val price: Double, val popularity: Int)

fun getProducts(): Set<Product> {
    return setOf(
        Product("one", 12.0, 12),
        Product("two", 13.0, 20),
        Product("three", 14.0, 50)
    )
}