package com.example.demo.services.kafka

import com.example.demo.services.database.city.CityService
import com.example.demo.services.kafka.dto.CityCreateDto
import kotlinx.serialization.json.Json
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class Consumer(
    private val cityService: CityService,
) {
    @KafkaListener(
        topics = ["#{'\${kafka.consumer.topics}'.split(',')}"],
        autoStartup = "\${kafka.consumer.auto-startup:false}",
    )
    fun handleCityCreate(@Payload message: String) {
        val cityCreateDto = Json.decodeFromString<CityCreateDto>(message)
        cityService.create(cityCreateDto)
    }
}