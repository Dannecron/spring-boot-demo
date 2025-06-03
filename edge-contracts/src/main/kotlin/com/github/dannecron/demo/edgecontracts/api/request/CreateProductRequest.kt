package com.github.dannecron.demo.edgecontracts.api.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequest(
    @field:NotBlank(message = "name value required")
    val name: String,
    val description: String?,
    @field:Min(value = 0, message = "price must be positive value")
    val price: Long,
)
