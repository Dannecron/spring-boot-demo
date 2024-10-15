package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException

interface Producer {
    @Throws(InvalidArgumentException::class)
    fun produceProductInfo(topicName: String, product: Product)
}
