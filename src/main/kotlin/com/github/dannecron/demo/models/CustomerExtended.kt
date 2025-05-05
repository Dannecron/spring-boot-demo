package com.github.dannecron.demo.models

import com.github.dannecron.demo.db.entity.City
import com.github.dannecron.demo.db.entity.Customer
import kotlinx.serialization.Serializable

@Serializable
data class CustomerExtended(
    val customer: Customer,
    val city: City?,
)
