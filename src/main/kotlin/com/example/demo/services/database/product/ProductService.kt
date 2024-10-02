package com.example.demo.services.database.product

import com.example.demo.models.Product
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.product.exceptions.ProductNotFoundException
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import org.springframework.stereotype.Service
import java.util.*

@Service
interface ProductService {
    fun findByGuid(guid: UUID): Product?

    fun create(name: String, price: Long, description: String?): Product

    @Throws(ProductNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): Product?

    @Throws(ProductNotFoundException::class, InvalidArgumentException::class)
    fun syncToKafka(guid: UUID, topic: String?)
}