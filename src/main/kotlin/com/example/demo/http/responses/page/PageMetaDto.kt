package com.example.demo.http.responses.page

import kotlinx.serialization.Serializable

@Serializable
data class PageMetaDto(
    val total: Long,
    val pages: Int,
)