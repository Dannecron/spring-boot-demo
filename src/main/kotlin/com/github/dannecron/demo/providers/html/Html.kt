package com.github.dannecron.demo.providers.html

class Html: Tag("html")

fun html(init: Html.() -> Unit): Html {
    val tag = Html()
    tag.init()
    return tag
}

fun Html.table(init : Table.() -> Unit) = doInit(
    Table(), init)
fun Html.center(init : Center.() -> Unit) = doInit(
    Center(), init)
