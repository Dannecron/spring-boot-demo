package com.github.dannecron.demo.core.services.neko

import com.github.dannecron.demo.core.dto.neko.ImageDto
import com.github.dannecron.demo.core.exceptions.neko.IntegrationException

interface NekoService {

    @Throws(IntegrationException::class)
    fun getCategories(): Set<String>

    @Throws(IntegrationException::class)
    fun getImages(category: String, amount: Int): List<ImageDto>
}
