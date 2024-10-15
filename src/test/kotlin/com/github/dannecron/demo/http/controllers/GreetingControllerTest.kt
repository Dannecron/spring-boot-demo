package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.BaseUnitTest
import org.hamcrest.core.StringContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest(com.github.dannecron.demo.http.controllers.GreetingController::class)
class GreetingControllerTest(@Autowired val mockMvc: MockMvc): BaseUnitTest() {
    @Test
    fun greetings_shouldSeeGreetingMessage() {
        mockMvc.get("/greeting")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType("text/plain;charset=UTF-8") } }
            .andExpect { content { string("Hello World!") } }
    }

    @Test
    fun exampleHtml_shouldSeeRenderedHtml() {
        mockMvc.get("/example/html")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType("text/html;charset=UTF-8") } }
            .andExpect { content { string(StringContains("Product")) } }
    }
}
