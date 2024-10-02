package com.example.demo.services.kafka

import com.example.demo.models.Product
import com.example.demo.services.kafka.exceptions.InvalidArgumentException

interface Producer {
    @Throws(InvalidArgumentException::class)
    fun produceProductInfo(topicName: String, product: Product)
}