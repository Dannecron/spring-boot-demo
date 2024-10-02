package com.example.demo.services.kafka.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CityCreateDto (
    @JsonProperty("guid")
    val guid: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("createdAt")
    val createdAt: String,
    @JsonProperty("updatedAt")
    val updatedAt: String?,
    @JsonProperty("deletedAt")
    val deletedAt: String?,
)
