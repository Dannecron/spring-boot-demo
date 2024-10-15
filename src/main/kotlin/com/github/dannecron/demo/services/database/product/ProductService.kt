package com.github.dannecron.demo.services.database.product

import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.ProductNotFoundException
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
interface ProductService {
    fun findByGuid(guid: UUID): Product?

    fun findAll(pageable: Pageable): Page<Product>

    fun create(name: String, price: Long, description: String?): Product

    @Throws(ProductNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): Product

    @Throws(ProductNotFoundException::class, InvalidArgumentException::class)
    fun syncToKafka(guid: UUID, topic: String?)
}
