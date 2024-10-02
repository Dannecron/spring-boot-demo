package com.example.demo.services.kafka

import com.example.demo.services.database.city.CityService
import com.example.demo.services.kafka.dto.CityCreateDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class Consumer(
    private val cityService: CityService,
    private val objectMapper: ObjectMapper
) {
    @KafkaListener(
        topics = ["#{'\${kafka.consumer.topics}'.split(',')}"],
        autoStartup = "\${kafka.consumer.auto-startup:false}",
    )
    fun handleCityCreate(@Payload message: String) {
        val cityCreateDto = objectMapper.readValue(message, CityCreateDto::class.java)
        cityService.create(cityCreateDto)
    }
}