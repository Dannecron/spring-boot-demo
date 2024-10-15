package com.github.dannecron.demo.providers.html

class TR: Tag("tr")

fun TR.td(color: String? = null, align : String = "left", init : TD.() -> Unit) = doInit(TD(), init)
    .set("align", align)
    .set("bgcolor", color)