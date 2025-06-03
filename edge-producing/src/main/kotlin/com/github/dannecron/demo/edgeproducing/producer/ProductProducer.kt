package com.github.dannecron.demo.edgeproducing.producer

import com.github.dannecron.demo.edgeproducing.dto.ProductDto
import com.github.dannecron.demo.edgeproducing.exceptions.InvalidArgumentException

interface ProductProducer {
    @Throws(InvalidArgumentException::class)
    fun produceProductSync(product: ProductDto)
}
