package com.github.dannecron.demo.services.neko

import com.github.dannecron.demo.services.neko.dto.ImagesResponse
import com.github.dannecron.demo.services.neko.exceptions.RequestException
import org.springframework.stereotype.Service

@Service
interface Client {
    @Throws(RequestException::class)
    fun getCategories(): Set<String>

    @Throws(RequestException::class)
    fun getImages(category: String, amount: Int): ImagesResponse
}
