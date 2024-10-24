package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.BaseUnitTest
import com.github.dannecron.demo.services.neko.Client
import com.github.dannecron.demo.services.neko.dto.Image
import com.github.dannecron.demo.services.neko.dto.ImagesResponse
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest(NekoController::class)
class NekoControllerTest(
    @Autowired val mockMvc: MockMvc,
): BaseUnitTest() {
    @MockBean
    private lateinit var nekoClient: Client

    @Test
    fun categories_success() {
        whenever(nekoClient.getCategories()) doReturn setOf("cat1", "cat2")

        mockMvc.get("/api/neko/categories")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { content { string("""["cat1","cat2"]""") } }
    }

    @Test
    fun images_success() {
        val category = "some"
        val animeName = "boku no pico"
        whenever(nekoClient.getImages(category = category, amount = 1)) doReturn ImagesResponse(
            results = listOf(
                Image(
                    "http://localhost",
                    animeName = animeName,
                )
            )
        )

        mockMvc.get("/api/neko/images/$category")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.images[0].animeName") { value(animeName) } }
            .andExpect { jsonPath("\$.images[0].artistName") { value(null) } }
    }
}
