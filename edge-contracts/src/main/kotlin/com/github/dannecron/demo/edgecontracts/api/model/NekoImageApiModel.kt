package com.github.dannecron.demo.edgecontracts.api.model

import kotlinx.serialization.Serializable

@Serializable
data class NekoImageApiModel(
    val url: String,
    val animeName: String?,
    val artistHref: String?,
    val artistName: String?,
    val sourceUrl: String?,
    val isGif: Boolean,
)
