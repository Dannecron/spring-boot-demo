package com.example.demo.services.kafka.dto

import kotlinx.serialization.Serializable

@Serializable
data class CityCreateDto (
    val guid: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
)
