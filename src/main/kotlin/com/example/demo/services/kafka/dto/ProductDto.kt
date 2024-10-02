package com.example.demo.services.kafka.dto

import com.example.demo.models.Product
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.format.DateTimeFormatter

data class ProductDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("guid")
    val guid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("price")
    val price: Long,
    @JsonProperty("createdAt")
    val createdAt: String,
    @JsonProperty("updatedAt")
    val updatedAt: String?,
    @JsonProperty("deletedAt")
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