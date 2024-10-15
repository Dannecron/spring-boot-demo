package com.github.dannecron.demo.services.database.city

import com.github.dannecron.demo.BaseDbTest
import com.github.dannecron.demo.models.City
import com.github.dannecron.demo.providers.CityRepository
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.util.*
import kotlin.test.*

@ContextConfiguration(classes = [CityRepository::class, CityServiceImpl::class])
class CityServiceImplDbTest: BaseDbTest() {
    @Autowired
    private lateinit var cityRepository: CityRepository
    @Autowired
    private lateinit var cityServiceImpl: CityServiceImpl

    @Test
    fun createFindDelete_success() {
        val name = "Some city"
        var city: City? = null

        try {
            city = cityServiceImpl.create(name = name)
            assertNotNull(city.id)
            assertEquals(name, city.name)

            val dbCity = cityServiceImpl.findByGuid(city.guid)
            assertNotNull(dbCity)
            assertEquals(city.id, dbCity.id)
            assertFalse(dbCity.isDeleted())

            val deletedCity = cityServiceImpl.delete(city.guid)
            assertEquals(city.id, deletedCity.id)
            assertNotNull(deletedCity.deletedAt)
            assertTrue(deletedCity.isDeleted())

            assertThrows<AlreadyDeletedException> {
                cityServiceImpl.delete(city.guid)
            }

            assertThrows<CityNotFoundException> {
                cityServiceImpl.delete(UUID.randomUUID())
            }
        } finally {
            val id = city?.id
            if (id != null) {
                cityRepository.deleteById(id)
            }

        }
    }
}
