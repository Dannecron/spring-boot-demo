package com.github.dannecron.demo.edgecontracts.api

import org.springframework.web.bind.annotation.GetMapping

interface GreetingApi {

    @GetMapping("/greeting")
    fun greet(): String

    @GetMapping(value = ["/example/html"], produces = ["text/html"])
    fun exampleHtml(): String
}
