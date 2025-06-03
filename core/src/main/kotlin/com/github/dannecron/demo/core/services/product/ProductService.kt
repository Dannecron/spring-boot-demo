package com.github.dannecron.demo.core.services.product

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.InvalidDataException
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ProductService {
    fun findByGuid(guid: UUID): Product?

    fun findAll(pageable: Pageable): Page<Product>

    fun create(name: String, price: Long, description: String?): Product

    @Throws(ProductNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): Product

    @Throws(ProductNotFoundException::class, InvalidDataException::class)
    fun send(guid: UUID, topic: String?)
}
