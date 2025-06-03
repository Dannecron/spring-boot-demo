package com.github.dannecron.demo.edgecontracts.api.response

import com.github.dannecron.demo.edgecontracts.api.model.CityApiModel
import com.github.dannecron.demo.edgecontracts.api.model.CustomerApiModel
import kotlinx.serialization.Serializable

@Serializable
data class GetCustomerResponse(
    val customer: CustomerApiModel,
    val city: CityApiModel?,
)
