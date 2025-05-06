package com.github.dannecron.demo.core.services.city

import com.github.dannecron.demo.core.dto.City
import com.github.dannecron.demo.core.dto.CityCreate
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.CityNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface CityService {
    fun findByGuid(guid: UUID): City?

    fun create(name: String): City
    fun create(cityCreate: CityCreate): City

    @Throws(CityNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): City
}
