package com.github.dannecron.demo.http.responses.neko

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    val animeName: String?,
    val artistHref: String?,
    val artistName: String?,
    val sourceUrl: String?,
) {
    fun isGif() = animeName != null
}
