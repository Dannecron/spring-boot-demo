package com.example.demo.services

import com.example.demo.exceptions.NotFoundException
import com.example.demo.exceptions.UnprocessableException
import com.example.demo.models.City
import com.example.demo.provider.CityRepository
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
        val city = findByGuid(guid) ?: throw NotFoundException()

        if (city.isDeleted()) {
            throw UnprocessableException("city already deleted")
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