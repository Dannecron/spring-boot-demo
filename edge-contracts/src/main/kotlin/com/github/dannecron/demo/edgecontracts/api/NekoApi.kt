package com.github.dannecron.demo.edgecontracts.api

import com.github.dannecron.demo.edgecontracts.api.response.GetNekoImagesResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping(value = ["/api/neko"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface NekoApi {

    @GetMapping("/categories")
    fun categories(): ResponseEntity<Set<String>>

    @GetMapping("/images/{category}")
    fun images(
        @PathVariable category: String,
        @RequestParam imagesCount: Int = 1,
    ): ResponseEntity<GetNekoImagesResponse>
}
