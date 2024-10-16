package com.github.dannecron.demo.providers

import com.github.dannecron.demo.models.OrderProduct
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OrderProductRepository: CrudRepository<OrderProduct, UUID>
