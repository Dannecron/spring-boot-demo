package com.github.dannecron.demo.core.dto

import java.time.OffsetDateTime

data class CityCreate(
    val guid: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val deletedAt: OffsetDateTime?,
)
