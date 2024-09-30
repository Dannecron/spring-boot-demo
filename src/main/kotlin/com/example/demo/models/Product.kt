package com.example.demo.models


import com.example.demo.models.serializables.OffsetDateTimeSerialization
import com.example.demo.models.serializables.UuidSerialization
import com.example.demo.utils.roundTo
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "product", schema = "public")
@Serializable
data class Product(
    @Id
    val id: Long?,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Long,
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
    fun getPriceDouble(): Double = (price.toDouble() / 100).roundTo(2)

    fun isDeleted(): Boolean = deletedAt != null
}