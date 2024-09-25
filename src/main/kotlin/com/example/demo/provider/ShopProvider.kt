package com.example.demo.provider

import com.example.demo.models.Shop

interface ShopProvider {
    fun getRandomShop(): Shop?
}