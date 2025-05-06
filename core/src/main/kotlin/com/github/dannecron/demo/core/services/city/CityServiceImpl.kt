package com.github.dannecron.demo.core.services.city

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.CityCreate
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.CityNotFoundException
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.CityEntity
import com.github.dannecron.demo.db.repository.CityRepository
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID

@Service
class CityServiceImpl(
    private val cityRepository: CityRepository,
    private val commonGenerator: CommonGenerator,
): CityService {
    override fun findByGuid(guid: UUID): City? = cityRepository.findByGuid(guid)?.toCore()

    override fun create(name: String): City = CityEntity(
        id = null,
        guid = commonGenerator.generateUUID(),
        name = name,
        createdAt = commonGenerator.generateCurrentTime(),
        updatedAt = null,
        deletedAt = null,
    ).let(cityRepository::save).toCore()

    override fun create(cityCreate: CityCreate): City = CityEntity(
        id = null,
        guid = UUID.fromString(cityCreate.guid),
        name = cityCreate.name,
        createdAt = cityCreate.createdAt,
        updatedAt = cityCreate.updatedAt,
        deletedAt = cityCreate.deletedAt,
    ).let(cityRepository::save).toCore()

    @Throws(CityNotFoundException::class, AlreadyDeletedException::class)
    override fun delete(guid: UUID): City {
        val city = findByGuid(guid) ?: throw CityNotFoundException()

        if (city.isDeleted()) {
            throw AlreadyDeletedException()
        }

        return cityRepository.save(
            city.copy(
                deletedAt = OffsetDateTime.now(),
            ).toEntity()
        ).toCore()
    }

    private fun CityEntity.toCore() = City(
        id = id!!,
        guid = guid,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )

    private fun City.toEntity() = CityEntity(
        id = id,
        guid = guid,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
}
