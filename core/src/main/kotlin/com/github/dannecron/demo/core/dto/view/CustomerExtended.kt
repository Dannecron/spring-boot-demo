package com.github.dannecron.demo.core.dto.view

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.Customer
import kotlinx.serialization.Serializable

@Serializable
data class CustomerExtended(
    val customer: Customer,
    val city: City?,
)
