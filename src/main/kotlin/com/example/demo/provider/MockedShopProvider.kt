package com.example.demo.provider

import com.example.demo.models.*
import java.time.OffsetDateTime
import java.util.*

class MockedShopProvider: ShopProvider {
    override fun getRandomShop(): Shop? {
        val productOne = makeProduct(id = 1, name = "one", price = 11.2)
        val productTwo = makeProduct(id = 2, name = "two", price = 13.2)
        val productThree = makeProduct(id = 3, name = "three", price = 15.2)
        val productFour = makeProduct(id = 4, name = "four", price = 14.2)

        return Shop(name="shop", customers= listOf(
            Customer(
                name = "Foo-1",
                city = makeCity(id = 1, name = "Foo"),
                orders = listOf(
                    Order(products = listOf(productOne, productTwo), isDelivered = true),
                    Order(products = listOf(productThree), isDelivered = false),
                )
            ),
            Customer(
                name = "Foo-2",
                city = makeCity(id = 2, name = "Bar"),
                orders = listOf(
                    Order(products = listOf(productOne), isDelivered = false),
                    Order(products = listOf(productTwo), isDelivered = true),
                    Order(products = listOf(productFour), isDelivered = true),
                )
            ),
        ))
    }

    private fun makeProduct(id: Long, name: String, price: Double): Product = Product(
        id = id,
        guid = UUID.randomUUID(),
        name = name,
        description = null,
        price = (price * 100).toLong(),
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )

    private fun makeCity(id: Long, name: String): City = City(
        id = id,
        guid = UUID.randomUUID(),
        name = name,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )
}