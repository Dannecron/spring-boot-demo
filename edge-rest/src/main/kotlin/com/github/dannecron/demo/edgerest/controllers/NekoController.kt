package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.core.dto.neko.ImageDto
import com.github.dannecron.demo.core.services.neko.NekoService
import com.github.dannecron.demo.edgecontracts.api.NekoApi
import com.github.dannecron.demo.edgecontracts.api.model.NekoImageApiModel
import com.github.dannecron.demo.edgecontracts.api.response.GetNekoImagesResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class NekoController(
    private val nekoService: NekoService
) : NekoApi {

    override fun categories(): ResponseEntity<Set<String>> =
        ResponseEntity(nekoService.getCategories(), HttpStatus.OK)

    override fun images(category: String, imagesCount: Int): ResponseEntity<GetNekoImagesResponse> =
        ResponseEntity(
            GetNekoImagesResponse(
                nekoService.getImages(category, imagesCount).map { it.toApiModel() }
            ),
            HttpStatus.OK,
        )

    private fun ImageDto.toApiModel() = NekoImageApiModel(
        url = url,
        animeName = animeName,
        artistHref = artistHref,
        artistName = artistName,
        sourceUrl = sourceUrl,
        isGif = animeName == null
    )
}
