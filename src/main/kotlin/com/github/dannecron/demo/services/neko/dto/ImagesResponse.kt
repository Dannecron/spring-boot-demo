package com.github.dannecron.demo.services.neko.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    val results: List<Image>
)
