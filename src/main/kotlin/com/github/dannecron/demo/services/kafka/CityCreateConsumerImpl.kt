package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.core.dto.CityCreate
import com.github.dannecron.demo.core.services.city.CityService
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import com.github.dannecron.demo.services.metrics.MetricsSender
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Component
class CityCreateConsumerImpl(
    private val cityService: CityService,
    private val metricsSender: MetricsSender,
) : CityCreateConsumer {

    override fun process(cityCreateDto: CityCreateDto) {
        cityService.create(cityCreateDto.toCore()).also {
            metricsSender.incrementConsumerCityCreate()
        }
    }

    private fun CityCreateDto.toCore() = CityCreate(
        guid = guid,
        name = name,
        createdAt = OffsetDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = updatedAt?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        },
        deletedAt = deletedAt?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
    )
}
