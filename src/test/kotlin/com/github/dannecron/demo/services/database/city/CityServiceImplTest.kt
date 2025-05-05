package com.github.dannecron.demo.services.database.city

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.City
import com.github.dannecron.demo.db.repository.CityRepository
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
        whenever(cityRepository.save(any<City>())).thenReturn(city)

        val result = cityServiceImpl.create("name")
        assertEquals(city, result)

        verify(cityRepository, times(1)).save(city.copy(id = null))
    }

    @Test
    fun `create - by dto`() {
        val cityGuid = UUID.randomUUID()
        val createdAt = OffsetDateTime.now()
        val cityCreate = CityCreateDto(
            guid = cityGuid.toString(),
            name = "name",
            createdAt = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = null,
            deletedAt = null,
        )

        val expectedCity = city.copy(guid = cityGuid, createdAt = createdAt)

        whenever(cityRepository.save(any<City>())).thenReturn(expectedCity)

        val result = cityServiceImpl.create(cityCreate)
        assertEquals(expectedCity, result)

        verify(cityRepository, times(1)).save(expectedCity.copy(id = null))
    }
}
