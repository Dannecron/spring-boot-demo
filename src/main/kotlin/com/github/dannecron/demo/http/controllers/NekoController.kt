package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.http.responses.neko.ImagesResponse
import com.github.dannecron.demo.services.neko.Client
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/neko"], produces = [MediaType.APPLICATION_JSON_VALUE])
class NekoController(
    private val nekoClient: Client,
) {
    @GetMapping("/categories")
    fun categories(): ResponseEntity<Any> = ResponseEntity(nekoClient.getCategories(), HttpStatus.OK)

    @GetMapping("/images/{category}")
    fun images(
        @PathVariable category: String,
        @RequestParam imagesCount: Int = 1,
    ): ResponseEntity<Any> = ResponseEntity(
        ImagesResponse(baseImages = nekoClient.getImages(category = category, amount = imagesCount)),
        HttpStatus.OK,
    )
}
