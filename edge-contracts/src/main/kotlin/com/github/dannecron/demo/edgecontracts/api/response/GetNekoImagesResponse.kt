package com.github.dannecron.demo.edgecontracts.api.response

import com.github.dannecron.demo.edgecontracts.api.model.NekoImageApiModel
import kotlinx.serialization.Serializable

@Serializable
data class GetNekoImagesResponse(
    val images: List<NekoImageApiModel>,
)
