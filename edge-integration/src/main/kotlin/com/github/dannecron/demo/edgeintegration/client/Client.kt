package com.github.dannecron.demo.edgeintegration.client

import com.github.dannecron.demo.edgeintegration.client.dto.ImagesResponse
import com.github.dannecron.demo.edgeintegration.client.exceptions.RequestException

interface Client {
    @Throws(RequestException::class)
    fun getCategories(): Set<String>

    @Throws(RequestException::class)
    fun getImages(category: String, amount: Int): ImagesResponse
}
