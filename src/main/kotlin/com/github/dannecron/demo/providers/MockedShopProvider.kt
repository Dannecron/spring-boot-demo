package com.github.dannecron.demo.providers

import com.github.dannecron.demo.models.*
import java.time.OffsetDateTime
import java.util.*

class MockedShopProvider: com.github.dannecron.demo.providers.ShopProvider {
    override fun getRandomShop(): Shop? {
        val productOne = makeProduct(id = 1, name = "one", price = 11.2)
        val productTwo = makeProduct(id = 2, name = "two", price = 13.2)
        val productThree = makeProduct(id = 3, name = "three", price = 15.2)
        val productFour = makeProduct(id = 4, name = "four", price = 14.2)

        return Shop(name="shop", customers= listOf(
            CustomerLocal(
                name = "Foo-1",
                city = makeCity(id = 1, name = "Foo"),
                orders = listOf(
                    OrderLocal(products = listOf(productOne, productTwo), isDelivered = true),
                    OrderLocal(products = listOf(productThree), isDelivered = false),
                )
            ),
            CustomerLocal(
                name = "Foo-2",
                city = makeCity(id = 2, name = "Bar"),
                orders = listOf(
                    OrderLocal(products = listOf(productOne), isDelivered = false),
                    OrderLocal(products = listOf(productTwo), isDelivered = true),
                    OrderLocal(products = listOf(productFour), isDelivered = true),
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
