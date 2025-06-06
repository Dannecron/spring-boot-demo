package com.github.dannecron.demo.providers.html

fun getTitleColor() = "#b9c9fe"
fun getCellColor(index: Int, row: Int) = if ((index + row) %2 == 0) "#dce4ff" else "#eff2ff"

fun renderProductTable(): String {
    return html {
        table {
            tr(color = getTitleColor()) {

                td {
                    text("Product")
                }
                td {
                    text("Price")
                }
                td {
                    text("Popularity")
                }
            }

            val products = getInnerProducts()
            for ((i, product) in products.withIndex()) {
                tr {
                    td(color = getCellColor(0, i + 1)) {
                        text(product.description)
                    }
                    td(color = getCellColor(1, i + 1)) {
                        text(product.price)
                    }
                    td(color = getCellColor(2, i + 1)) {
                        text(product.popularity)
                    }
                }
            }
        }
    }.toString()
}
