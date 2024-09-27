package com.example.demo.requests

data class CreateProductRequest(
    val name: String,
    val description: String?,
    val price: Long,
)
