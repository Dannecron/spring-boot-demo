package com.github.dannecron.demo.providers.html

class Table: Tag("table")

fun Table.tr(color: String? = null, init : TR.() -> Unit) = doInit(TR(), init).set("bgcolor", color)
