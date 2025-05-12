package com.github.dannecron.demo.providers.html

open class Tag(val name: String) {
    val children: MutableList<Tag> = mutableListOf()
    val attributes: MutableList<Attribute> = mutableListOf()

    override fun toString(): String {
        return "<$name" +
                (if (attributes.isEmpty()) "" else attributes.joinToString(separator = "", prefix = " ")) + ">" +
                (if (children.isEmpty()) "" else children.joinToString(separator = "")) +
                "</$name>"
    }
}

fun <T: Tag> T.set(name: String, value: String?): T {
    if (value != null) {
        attributes.add(Attribute(name, value))
    }
    return this
}

fun <T: Tag> Tag.doInit(tag: T, init: T.() -> Unit): T {
    tag.init()
    children.add(tag)
    return tag
}

fun Tag.text(s : Any?) = doInit(Text(s.toString())) {}
