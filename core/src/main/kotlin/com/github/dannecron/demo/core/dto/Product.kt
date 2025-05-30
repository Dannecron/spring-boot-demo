package com.github.dannecron.demo.core.dto

import com.github.dannecron.demo.db.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.db.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.math.pow
import kotlin.math.roundToInt

@Serializable
data class Product(
    val id: Long,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Long,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val updatedAt: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    val deletedAt: OffsetDateTime?,
) {
    fun getPriceDouble(): Double = (price.toDouble() / 100).roundTo(2)

    fun isDeleted(): Boolean = deletedAt != null

    private fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }
}
