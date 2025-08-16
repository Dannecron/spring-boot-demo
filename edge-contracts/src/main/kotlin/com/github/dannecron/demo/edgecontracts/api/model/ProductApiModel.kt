package com.github.dannecron.demo.edgecontracts.api.model

import com.github.dannecron.demo.edgecontracts.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.edgecontracts.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class ProductApiModel(
    val id: Long,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Long,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val updatedAt: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val deletedAt: OffsetDateTime?,
    val priceDouble: Double,
    val isDeleted: Boolean,
)
