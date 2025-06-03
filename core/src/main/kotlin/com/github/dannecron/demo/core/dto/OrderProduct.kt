package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime
import java.util.UUID

data class OrderProduct(
    val guid: UUID,
    val orderId: Long,
    val productId: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
)
