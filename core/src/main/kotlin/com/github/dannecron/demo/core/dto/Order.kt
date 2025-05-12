package com.github.dannecron.demo.core.dto

import com.github.dannecron.demo.db.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.db.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class Order(
    val id: Long,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val customerId: Long,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val deliveredAt: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val updatedAt: OffsetDateTime?,
) {
    fun isDelivered(): Boolean = deliveredAt != null
}
