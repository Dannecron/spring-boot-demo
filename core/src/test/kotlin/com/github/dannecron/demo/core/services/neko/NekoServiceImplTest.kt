package com.github.dannecron.demo.core.services.neko

import com.github.dannecron.demo.core.exceptions.neko.IntegrationException
import com.github.dannecron.demo.edgeintegration.client.neko.Client
import com.github.dannecron.demo.edgeintegration.client.neko.dto.Image
import com.github.dannecron.demo.edgeintegration.client.neko.dto.ImagesResponse
import com.github.dannecron.demo.edgeintegration.client.neko.exceptions.RequestException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * AI generated
 */
class NekoServiceImplTest {

    private val nekoClient: Client = mock()
    private val nekoService: NekoService = NekoServiceImpl(nekoClient)

    @Test
    fun `getCategories - success`() {
        // Given
        val expectedCategories = setOf("neko", "wink", "hug")
        whenever(nekoClient.getCategories()).doReturn(expectedCategories)

        // When
        val result = nekoService.getCategories()

        // Then
        assertEquals(expectedCategories, result)
        verify(nekoClient, times(1)).getCategories()
    }

    @Test
    fun `getCategories - throws IntegrationException when client throws RequestException`() {
        // Given
        val requestException = RequestException("Client error")
        whenever(nekoClient.getCategories()).doThrow(requestException)

        // When & Then
        val exception = assertThrows<IntegrationException> {
            nekoService.getCategories()
        }

        assertEquals("Neko request error", exception.message)
        assertEquals(requestException, exception.cause)
        verify(nekoClient, times(1)).getCategories()
    }

    @Test
    fun `getImages - success - maps images correctly`() {
        // Given
        val category = "neko"
        val amount = 2
        val image1 = Image(
            url = "https://example.com/image1.png",
            animeName = "Test Anime 1",
            artistHref = "https://artist1.com",
            artistName = "Artist 1",
            sourceUrl = "https://source1.com"
        )
        val image2 = Image(
            url = "https://example.com/image2.gif",
            animeName = null,
            artistHref = null,
            artistName = null,
            sourceUrl = null
        )
        val imagesResponse = ImagesResponse(results = listOf(image1, image2))
        whenever(nekoClient.getImages(category, amount)).doReturn(imagesResponse)

        // When
        val result = nekoService.getImages(category, amount)

        // Then
        assertEquals(2, result.size)
        
        val firstImage = result[0]
        assertEquals("https://example.com/image1.png", firstImage.url)
        assertEquals("Test Anime 1", firstImage.animeName)
        assertEquals("https://artist1.com", firstImage.artistHref)
        assertEquals("Artist 1", firstImage.artistName)
        assertEquals("https://source1.com", firstImage.sourceUrl)

        val secondImage = result[1]
        assertEquals("https://example.com/image2.gif", secondImage.url)
        assertEquals(null, secondImage.animeName)
        assertEquals(null, secondImage.artistHref)
        assertEquals(null, secondImage.artistName)
        assertEquals(null, secondImage.sourceUrl)

        verify(nekoClient, times(1)).getImages(category, amount)
    }

    @Test
    fun `getImages - success - empty results`() {
        // Given
        val category = "empty"
        val amount = 1
        val imagesResponse = ImagesResponse(results = emptyList())
        whenever(nekoClient.getImages(category, amount)).doReturn(imagesResponse)

        // When
        val result = nekoService.getImages(category, amount)

        // Then
        assertTrue(result.isEmpty())
        verify(nekoClient, times(1)).getImages(category, amount)
    }

    @Test
    fun `getImages - throws IntegrationException when client throws RequestException`() {
        // Given
        val category = "neko"
        val amount = 5
        val requestException = RequestException("Client error")
        whenever(nekoClient.getImages(category, amount)).doThrow(requestException)

        // When & Then
        val exception = assertThrows<IntegrationException> {
            nekoService.getImages(category, amount)
        }

        assertEquals("Neko request error", exception.message)
        assertEquals(requestException, exception.cause)
        verify(nekoClient, times(1)).getImages(category, amount)
    }

    @Test
    fun `getImages - success - single image with all fields null`() {
        // Given
        val category = "test"
        val amount = 1
        val image = Image(
            url = "https://example.com/minimal.jpg",
            animeName = null,
            artistHref = null,
            artistName = null,
            sourceUrl = null
        )
        val imagesResponse = ImagesResponse(results = listOf(image))
        whenever(nekoClient.getImages(category, amount)).doReturn(imagesResponse)

        // When
        val result = nekoService.getImages(category, amount)

        // Then
        assertEquals(1, result.size)
        val resultImage = result[0]
        assertEquals("https://example.com/minimal.jpg", resultImage.url)
        assertEquals(null, resultImage.animeName)
        assertEquals(null, resultImage.artistHref)
        assertEquals(null, resultImage.artistName)
        assertEquals(null, resultImage.sourceUrl)

        verify(nekoClient, times(1)).getImages(category, amount)
    }
} 
