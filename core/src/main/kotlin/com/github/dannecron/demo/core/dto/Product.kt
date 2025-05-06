package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime
import java.util.UUID
import kotlin.math.pow
import kotlin.math.roundToInt

data class Product(
    val id: Long,
    val guid: UUID,
    val name: String,
    val description: String?,
    val price: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val deletedAt: OffsetDateTime?,
) {
    fun getPriceDouble(): Double = (price.toDouble() / 100).roundTo(2)

    fun isDeleted(): Boolean = deletedAt != null

    private fun Double.roundTo(numFractionDigits: Int): Double {
        val factor = 10.0.pow(numFractionDigits.toDouble())
        return (this * factor).roundToInt() / factor
    }
}
