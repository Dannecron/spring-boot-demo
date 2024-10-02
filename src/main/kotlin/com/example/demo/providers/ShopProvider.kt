package com.example.demo.providers

import com.example.demo.models.Shop

interface ShopProvider {
    fun getRandomShop(): Shop?
}