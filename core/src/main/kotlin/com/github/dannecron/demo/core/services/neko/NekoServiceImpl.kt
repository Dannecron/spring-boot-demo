package com.github.dannecron.demo.core.services.neko

import com.github.dannecron.demo.core.dto.neko.ImageDto
import com.github.dannecron.demo.core.exceptions.neko.IntegrationException
import com.github.dannecron.demo.edgeintegration.client.neko.Client
import com.github.dannecron.demo.edgeintegration.client.neko.dto.Image
import com.github.dannecron.demo.edgeintegration.client.neko.exceptions.RequestException
import org.springframework.stereotype.Service

@Service
class NekoServiceImpl(
    private val nekoClient: Client
) : NekoService {
    override fun getCategories(): Set<String> =
        try {
            nekoClient.getCategories()
        } catch (ex: RequestException) {
            throw IntegrationException("Neko request error", ex)
        }

    override fun getImages(category: String, amount: Int): List<ImageDto> =
        try {
            nekoClient.getImages(category, amount).results.map { it.toCoreDto()}
        } catch (ex: RequestException) {
            throw IntegrationException("Neko request error", ex)
        }

    private fun Image.toCoreDto() = ImageDto(
        url = url,
        animeName = animeName,
        artistHref = artistHref,
        artistName = artistName,
        sourceUrl = sourceUrl,
    )
}
