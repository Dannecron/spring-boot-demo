package com.github.dannecron.demo.core.services.city

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.CityCreate
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.CityEntity
import com.github.dannecron.demo.db.repository.CityRepository
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CityServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }
    private val cityRepository: CityRepository = mock()
    private val cityServiceImpl = CityServiceImpl(cityRepository, commonGenerator)

    private val cityEntity = CityEntity(
        id = 1000,
        guid = mockGuid,
        name = "name",
        createdAt = mockCurrentTime,
        updatedAt = null,
        deletedAt = null,
    )
    private val city = City(
        id = 1000,
        guid = mockGuid,
        name = "name",
        createdAt = mockCurrentTime,
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    fun `create - by name`() {
        whenever(cityRepository.save(any<CityEntity>())).thenReturn(cityEntity)

        val result = cityServiceImpl.create("name")
        assertEquals(city, result)

        verify(cityRepository, times(1)).save(cityEntity.copy(id = null))
    }

    @Test
    fun `create - by dto`() {
        val cityGuid = UUID.randomUUID()
        val createdAt = OffsetDateTime.now()
        val cityCreate = CityCreate(
            guid = cityGuid.toString(),
            name = "name",
            createdAt = createdAt,
            updatedAt = null,
            deletedAt = null,
        )

        val expectedCityEntity = cityEntity.copy(guid = cityGuid, createdAt = createdAt)
        val expectedCity = city.copy(guid = cityGuid, createdAt = createdAt)

        whenever(cityRepository.save(any<CityEntity>())).thenReturn(expectedCityEntity)

        val result = cityServiceImpl.create(cityCreate)
        assertEquals(expectedCity, result)

        verify(cityRepository, times(1)).save(expectedCityEntity.copy(id = null))
    }
}
