package com.example.demo.services

import com.example.demo.exceptions.NotFoundException
import com.example.demo.exceptions.UnprocessableException
import com.example.demo.models.Product
import com.example.demo.provider.ProductRepository
import java.time.OffsetDateTime
import java.util.*

class ProductServiceImpl(private val productRepository: ProductRepository): ProductService {
    override fun findByGuid(guid: UUID): Product? = productRepository.findByGuid(guid)

    override fun create(name: String, price: Long, description: String?): Product {
        val product = Product(
            id = null,
            guid = UUID.randomUUID(),
            name = name,
            description = description,
            price = price,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = null,
        )

        return productRepository.save(product)
    }

    override fun delete(guid: UUID): Product? {
        val product = findByGuid(guid) ?: throw NotFoundException()

        if (product.isDeleted()) {
            throw UnprocessableException("product already deleted")
        }

        val deletedProduct = product.copy(
            id = product.id!!,
            guid = product.guid,
            name = product.name,
            description = product.description,
            price = product.price,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
            deletedAt = OffsetDateTime.now(),
        )

        return productRepository.save(deletedProduct)
    }
}