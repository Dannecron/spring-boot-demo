package com.github.dannecron.demo.providers.html

class Html: com.github.dannecron.demo.providers.html.Tag("html")

fun html(init: com.github.dannecron.demo.providers.html.Html.() -> Unit): com.github.dannecron.demo.providers.html.Html {
    val tag = com.github.dannecron.demo.providers.html.Html()
    tag.init()
    return tag
}

fun com.github.dannecron.demo.providers.html.Html.table(init : com.github.dannecron.demo.providers.html.Table.() -> Unit) = doInit(
    com.github.dannecron.demo.providers.html.Table(), init)
fun com.github.dannecron.demo.providers.html.Html.center(init : com.github.dannecron.demo.providers.html.Center.() -> Unit) = doInit(
    com.github.dannecron.demo.providers.html.Center(), init)
