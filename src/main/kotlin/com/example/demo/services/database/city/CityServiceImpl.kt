package com.example.demo.services.database.city

import com.example.demo.models.City
import com.example.demo.providers.CityRepository
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.city.exceptions.CityNotFoundException
import java.time.OffsetDateTime
import java.util.*

class CityServiceImpl(
    private val cityRepository: CityRepository
): CityService {
    override fun findByGuid(guid: UUID): City? = cityRepository.findByGuid(guid)

    override fun create(name: String): City? {
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

    override fun delete(guid: UUID): City? {
        val city = findByGuid(guid) ?: throw CityNotFoundException()

        if (city.isDeleted()) {
            throw AlreadyDeletedException()
        }

        val deletedCity = city.copy(
            id = city.id!!,
            guid = city.guid,
            name = city.name,
            createdAt = city.createdAt,
            updatedAt = city.updatedAt,
            deletedAt = OffsetDateTime.now(),
        )

        return cityRepository.save(deletedCity)
    }
}