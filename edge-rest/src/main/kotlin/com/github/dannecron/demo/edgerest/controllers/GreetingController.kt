package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.edgecontracts.api.GreetingApi
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController() : GreetingApi {
    override fun greet(): String {
        return "Hello World!"
    }

    @ResponseBody
    override fun exampleHtml(): String {
        TODO("Not yet implemented")
    }
}
