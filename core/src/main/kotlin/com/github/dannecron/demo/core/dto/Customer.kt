package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime
import java.util.UUID

data class Customer(
    val id: Long,
    val guid: UUID,
    val name: String,
    val cityId: Long?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
)
