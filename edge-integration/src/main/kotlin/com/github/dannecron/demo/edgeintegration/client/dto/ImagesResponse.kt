package com.github.dannecron.demo.edgeintegration.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val results: List<Image>
)
