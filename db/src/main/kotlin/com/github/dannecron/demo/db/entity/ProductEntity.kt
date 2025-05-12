package com.github.dannecron.demo.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "product")
data class ProductEntity(
    @Id
    val id: Long?,
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Long,
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?,
    @Column(value = "deleted_at")
    val deletedAt: OffsetDateTime?,
)
