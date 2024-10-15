package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.providers.html.renderProductTable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {
    @GetMapping("/greeting")
    fun greet(): String {
        return "Hello World!"
    }

    @GetMapping(value = ["/example/html"], produces = ["text/html"])
    @ResponseBody
    fun exampleHtml(): String {
        return renderProductTable()
    }
}
