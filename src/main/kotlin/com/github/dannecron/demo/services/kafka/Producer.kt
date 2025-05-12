package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.services.kafka.dto.ProductDto
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException

interface Producer {
    @Throws(InvalidArgumentException::class)
    fun produceProductSync(product: ProductDto)
}
