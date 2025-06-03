package com.github.dannecron.demo.edgecontracts.api.response.page

import kotlinx.serialization.Serializable

@Serializable
data class PageMetaDto(
    val total: Long,
    val pages: Int,
)
