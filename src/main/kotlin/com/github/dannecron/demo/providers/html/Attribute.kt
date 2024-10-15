package com.github.dannecron.demo.providers.html

class Attribute(val name : String, val value : String) {
    override fun toString() = """$name="$value" """
}
