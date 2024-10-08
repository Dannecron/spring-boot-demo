package com.example.demo.services.kafka

import com.example.demo.services.database.city.CityService
import com.example.demo.services.kafka.dto.CityCreateDto
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.serialization.json.Json
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class Consumer(
    private val cityService: CityService,
    private val metricRegistry: MeterRegistry,
) {
    @KafkaListener(
        topics = ["#{'\${kafka.consumer.topics}'.split(',')}"],
        autoStartup = "\${kafka.consumer.auto-startup:false}",
    )
    fun handleCityCreate(@Payload message: String) {
        val cityCreateDto = Json.decodeFromString<CityCreateDto>(message)
            .also {
                val counter = Counter.builder("kafka_consumer_city_create")
                    .description("consumed created city event")
                    .register(metricRegistry)
                counter.increment()
            }

        cityService.create(cityCreateDto)
    }
}