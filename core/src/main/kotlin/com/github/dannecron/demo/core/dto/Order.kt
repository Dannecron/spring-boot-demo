package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime
import java.util.UUID

data class Order(
    val id: Long,
    val guid: UUID,
    val customerId: Long,
    val deliveredAt: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
) {
    fun isDelivered(): Boolean = deliveredAt != null
}
