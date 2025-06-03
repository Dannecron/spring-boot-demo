package com.github.dannecron.demo.edgeintegration.client.neko

import com.github.dannecron.demo.edgeintegration.client.neko.dto.ImagesResponse
import com.github.dannecron.demo.edgeintegration.client.neko.exceptions.RequestException

interface Client {
    @Throws(RequestException::class)
    fun getCategories(): Set<String>

    @Throws(RequestException::class)
    fun getImages(category: String, amount: Int): ImagesResponse
}
