package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.BaseDbTest
import com.github.dannecron.demo.db.entity.CityEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ContextConfiguration(classes = [CityRepository::class])
class CityEntityRepositoryTest : BaseDbTest() {

    @Autowired
    private lateinit var cityRepository: CityRepository

    private val cityGuid = UUID.fromString("21a1a3a8-621a-40f7-b64f-7e118aa241b9")
    private val cityEntity = CityEntity(
        id = 1000,
        guid = cityGuid,
        name = "Tokyo",
        createdAt = OffsetDateTime.parse("2025-01-01T12:10:05+00:00"),
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    @Sql(scripts = ["/sql/insert_city.sql"])
    fun findByGuid() {
        val result = cityRepository.findByGuid(cityGuid)
        assertEquals(cityEntity, result)

        val emptyResult = cityRepository.findByGuid(UUID.randomUUID())
        assertNull(emptyResult)
    }

    @Test
    @Sql(scripts = ["/sql/insert_city.sql"])
    fun softDelete() {
        val deletedAt = OffsetDateTime.parse("2025-01-02T12:10:05+00:00")
        val expectedCity = cityEntity.copy(deletedAt = deletedAt)

        val result = cityRepository.softDelete(cityGuid, deletedAt)
        assertEquals(expectedCity, result)
    }
}
