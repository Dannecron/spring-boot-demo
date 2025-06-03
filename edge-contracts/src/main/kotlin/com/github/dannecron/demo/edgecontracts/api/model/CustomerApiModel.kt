package com.github.dannecron.demo.edgecontracts.api.model

import com.github.dannecron.demo.db.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.db.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class CustomerApiModel(
    val id: Long,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    val cityId: Long?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val updatedAt: OffsetDateTime?,
)
