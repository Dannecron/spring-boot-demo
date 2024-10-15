package com.github.dannecron.demo.providers

import com.github.dannecron.demo.models.Shop

interface ShopProvider {
    fun getRandomShop(): Shop?
}
