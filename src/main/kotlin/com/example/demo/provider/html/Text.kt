package com.example.demo.provider.html

class Text(val text: String): Tag("b") {
    override fun toString() = text
}