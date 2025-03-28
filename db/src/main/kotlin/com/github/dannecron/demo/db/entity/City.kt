package com.github.dannecron.demo.db.entity

import com.github.dannecron.demo.db.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.db.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table("city")
@Serializable
data class City(
    @Id
    val id: Long?,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "deleted_at")
    val deletedAt: OffsetDateTime?,
) {
    fun isDeleted(): Boolean = deletedAt != null
}
