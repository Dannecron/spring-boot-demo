package com.github.dannecron.demo.http.responses.neko

import kotlinx.serialization.Serializable
import com.github.dannecron.demo.services.neko.dto.ImagesResponse as BaseResponse

@Serializable
data class ImagesResponse(
    val images: List<Image>,
) {
    constructor(baseImages: BaseResponse): this(
        images = baseImages.results.map {
            Image(it.url, it.animeName, it.artistHref, it.artistName, it.sourceUrl)
        }
    )
}
