package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.core.dto.neko.ImageDto
import com.github.dannecron.demo.core.exceptions.neko.IntegrationException
import com.github.dannecron.demo.core.services.neko.NekoService
import com.github.dannecron.demo.edgerest.ExceptionHandler
import com.github.dannecron.demo.edgerest.WebTestConfig
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest(NekoController::class)
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = [
        WebTestConfig::class,
        NekoController::class,
        ExceptionHandler::class,
    ]
)
class NekoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var nekoService: NekoService

    @Test
    fun `getCategories - success`() {
        whenever(nekoService.getCategories()).thenReturn(setOf("cat1", "cat2"))

        mockMvc.get("/api/neko/categories")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { content { string("""["cat1","cat2"]""") } }

        verify(nekoService, times(1)).getCategories()
    }

    @Test
    fun `getCategories - fail - integration error`() {
        whenever(nekoService.getCategories()).thenThrow(IntegrationException("some error"))

        mockMvc.get("/api/neko/categories")
            .andExpect { status { isInternalServerError() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("$.status") { value("internal") } }

        verify(nekoService, times(1)).getCategories()
    }

    @Test
    fun `getImages - success`() {
        val category = "some"
        val animeName = "boku no pico"
        whenever(nekoService.getImages(category = category, amount = 1))
            .thenReturn(
                listOf(
                    ImageDto(
                        "http://localhost",
                        animeName = animeName,
                        artistHref = null,
                        artistName = null,
                        sourceUrl = null,
                    )
                )
            )

        mockMvc.get("/api/neko/images/$category")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andExpect { jsonPath("\$.images[0].animeName") { value(animeName) } }
            .andExpect { jsonPath("\$.images[0].artistName") { value(null) } }

        verify(nekoService, times(1)).getImages(category = category, amount = 1)
    }
}
