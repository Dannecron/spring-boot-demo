package com.example.demo.providers.html

class Text(val text: String): Tag("b") {
    override fun toString() = text
}