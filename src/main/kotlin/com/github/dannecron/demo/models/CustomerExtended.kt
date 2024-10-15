package com.github.dannecron.demo.models

import kotlinx.serialization.Serializable

@Serializable
data class CustomerExtended(
    val customer: Customer,
    val city: City?,
)
