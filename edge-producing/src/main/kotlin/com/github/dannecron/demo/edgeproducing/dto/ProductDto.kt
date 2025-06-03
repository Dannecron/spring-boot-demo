package com.github.dannecron.demo.edgeproducing.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Long,
    val guid: String,
    val name: String,
    val description: String?,
    val price: Long,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
)
