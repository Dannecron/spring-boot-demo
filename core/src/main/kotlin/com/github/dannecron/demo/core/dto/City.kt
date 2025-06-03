package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime
import java.util.UUID

data class City(
    val id: Long,
    val guid: UUID,
    val name: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val deletedAt: OffsetDateTime?,
) {
    fun isDeleted(): Boolean = deletedAt != null
}
