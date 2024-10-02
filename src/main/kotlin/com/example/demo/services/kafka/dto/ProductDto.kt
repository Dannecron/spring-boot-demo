package com.example.demo.services.kafka.dto

import com.example.demo.models.Product
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import java.time.format.DateTimeFormatter

data class ProductDto(
    val id: Long,
    val guid: String,
    val name: String,
    val description: String?,
    val price: Long,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
) {
    @Throws(InvalidArgumentException::class)
    constructor(product: Product) : this(
        id = product.id ?: throw InvalidArgumentException("product.id"),
        guid = product.guid.toString(),
        name = product.name,
        description = product.description,
        price = product.price,
        createdAt = product.createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = product.updatedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        deletedAt = product.deletedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    )
}