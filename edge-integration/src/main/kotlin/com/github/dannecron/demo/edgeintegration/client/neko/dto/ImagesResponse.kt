package com.github.dannecron.demo.edgeintegration.client.neko.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val results: List<Image>
)
