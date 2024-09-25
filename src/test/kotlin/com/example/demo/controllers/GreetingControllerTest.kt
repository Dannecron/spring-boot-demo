package com.example.demo.controllers

import org.hamcrest.core.StringContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(GreetingController::class)
class GreetingControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun greetings_shouldSeeGreetingMessage() {
        mockMvc.perform(get("/greeting"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("text/plain;charset=UTF-8"))
            .andExpect(content().string("Hello World!"))
    }

    @Test
    fun exampleHtml_shouldSeeRenderedHtml() {
        mockMvc.perform(get("/example/html"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(StringContains("Product")))
    }
}
