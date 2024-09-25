package com.example.demo.models


import com.example.demo.models.serializables.OffsetDateTimeSerialization
import com.example.demo.models.serializables.UuidSerialization
import com.example.demo.utils.roundTo
import jakarta.persistence.Column
import jakarta.persistence.Id
import kotlinx.serialization.Serializable
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "product", schema = "public")
@Serializable
data class Product(
    @Id var id: Long,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Int,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(name = "created_at")
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(name = "updated_at")
    val updatedAt: OffsetDateTime?,
) {
    fun getPriceDouble(): Double = (price.toDouble() / 100).roundTo(2)
}