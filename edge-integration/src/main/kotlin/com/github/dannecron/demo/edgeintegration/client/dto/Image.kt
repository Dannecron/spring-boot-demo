package com.github.dannecron.demo.edgeintegration.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    @SerialName("anime_name")
    val animeName: String? = null,
    @SerialName("artist_href")
    val artistHref: String? = null,
    @SerialName("artist_name")
    val artistName: String? = null,
    @SerialName("source_url")
    val sourceUrl: String? = null,
)
