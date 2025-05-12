package com.github.dannecron.demo.db.entity.order

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "order")
data class OrderEntity(
    @Id
    val id: Long?,
    val guid: UUID,
    val customerId: Long,
    @Column(value = "delivered_at")
    val deliveredAt: OffsetDateTime?,
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?
)
