package com.github.dannecron.demo.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table("customer")
data class CustomerEntity(
    @Id
    val id: Long?,
    val guid: UUID,
    val name: String,
    val cityId: Long?,
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?,
)
