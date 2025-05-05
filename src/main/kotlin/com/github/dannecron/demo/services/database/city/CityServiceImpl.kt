package com.github.dannecron.demo.services.database.city

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.City
import com.github.dannecron.demo.db.repository.CityRepository
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class CityServiceImpl(
    private val cityRepository: CityRepository,
    private val commonGenerator: CommonGenerator,
): CityService {
    override fun findByGuid(guid: UUID): City? = cityRepository.findByGuid(guid)

    override fun create(name: String): City = City(
        id = null,
        guid = commonGenerator.generateUUID(),
        name = name,
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
        deletedAt = null,
    ).let {
        cityRepository.save(it)
    }

    override fun create(kafkaCityDto: CityCreateDto): City = City(
        id = null,
        guid = UUID.fromString(kafkaCityDto.guid),
        name = kafkaCityDto.name,
        createdAt = OffsetDateTime.parse(kafkaCityDto.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = kafkaCityDto.deletedAt?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        },
        deletedAt = kafkaCityDto.deletedAt?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        },
    ).let {
        cityRepository.save(it)
    }

    override fun delete(guid: UUID): City {
        val city = findByGuid(guid) ?: throw CityNotFoundException()

        if (city.isDeleted()) {
            throw AlreadyDeletedException()
        }

        return cityRepository.save(
            city.copy(
                deletedAt = OffsetDateTime.now(),
            )
        )
    }
}
