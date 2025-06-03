package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.edgerest.ExceptionHandler
import com.github.dannecron.demo.edgerest.WebTestConfig
import org.hamcrest.core.StringContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Ignore
import kotlin.test.Test

@WebMvcTest(GreetingController::class)
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = [
        WebTestConfig::class,
        GreetingController::class,
        ExceptionHandler::class,
    ]
)
class GreetingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun greetings_shouldSeeGreetingMessage() {
        mockMvc.get("/greeting")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType("text/plain;charset=UTF-8") } }
            .andExpect { content { string("Hello World!") } }
    }

    @Test
    @Ignore
    fun exampleHtml_shouldSeeRenderedHtml() {
        mockMvc.get("/example/html")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType("text/html;charset=UTF-8") } }
            .andExpect { content { string(StringContains("Product")) } }
    }
}
