package com.github.dannecron.demo.providers

import com.github.dannecron.demo.models.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CrudRepository<Order, Long>
