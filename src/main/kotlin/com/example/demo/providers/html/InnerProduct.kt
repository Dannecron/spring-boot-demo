package com.example.demo.providers.html

data class InnerProduct(val description: String, val price: Double, val popularity: Int)

fun getInnerProducts(): Set<InnerProduct> {
    return setOf(
        InnerProduct("one", 12.0, 12),
        InnerProduct("two", 13.0, 20),
        InnerProduct("three", 14.0, 50)
    )
}