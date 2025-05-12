package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Order
import com.github.dannecron.demo.core.dto.OrderProduct
import com.github.dannecron.demo.core.dto.Product

interface OrderProductService {
    fun create(order: Order, product: Product): OrderProduct
}
