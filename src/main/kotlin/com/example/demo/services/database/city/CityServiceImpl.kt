package com.example.demo.services.database.city

import com.example.demo.models.City
import com.example.demo.providers.CityRepository
import com.example.demo.services.database.exceptions.CityNotFoundException
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.kafka.dto.CityCreateDto
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CityServiceImpl(
    private val cityRepository: CityRepository
): CityService {
    override fun findByGuid(guid: UUID): City? = cityRepository.findByGuid(guid)

    override fun create(name: String): City {
        val city = City(
            id = null,
            guid = UUID.randomUUID(),
            name = name,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            deletedAt = null,
        )

        return cityRepository.save(city)
    }

    override fun create(kafkaCityDto: CityCreateDto): City {
        val updatedAt = kafkaCityDto.updatedAt
        val deletedAt = kafkaCityDto.deletedAt

        val city = City(
            id = null,
            guid = UUID.fromString(kafkaCityDto.guid),
            name = kafkaCityDto.name,
            createdAt = OffsetDateTime.parse(kafkaCityDto.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = if (updatedAt != null) OffsetDateTime.parse(updatedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME) else null,
            deletedAt = if (deletedAt != null) OffsetDateTime.parse(deletedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME) else null,
        )

        return cityRepository.save(city)
    }

    override fun delete(guid: UUID): City {
        val city = findByGuid(guid) ?: throw CityNotFoundException()

        if (city.isDeleted()) {
            throw AlreadyDeletedException()
        }

        val deletedCity = city.copy(
            deletedAt = OffsetDateTime.now(),
        )

        return cityRepository.save(deletedCity)
    }
}
