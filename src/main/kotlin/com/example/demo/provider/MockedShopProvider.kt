package com.example.demo.provider

import com.example.demo.models.*

class MockedShopProvider: ShopProvider {
    override fun getRandomShop(): Shop? {
        return Shop(name="shop", customers= listOf(
            Customer(
                name = "Foo-1",
                city = City(name= "Bar"),
                orders = listOf(
                    Order(products = listOf(Product(name = "one", price = 11.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "two", price = 13.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "three", price = 15.2)), isDelivered = true),
                )
            ),
            Customer(
                name = "Foo-2",
                city = City(name= "Bar"),
                orders = listOf(
                    Order(products = listOf(Product(name = "one", price = 12.2)), isDelivered = false),
                    Order(products = listOf(Product(name = "two", price = 13.2)), isDelivered = true),
                    Order(products = listOf(Product(name = "four", price = 14.2)), isDelivered = true),
                )
            ),
        ))
    }
}