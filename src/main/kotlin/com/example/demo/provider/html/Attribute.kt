package com.example.demo.provider.html

class Attribute(val name : String, val value : String) {
    override fun toString() = """$name="$value" """
}