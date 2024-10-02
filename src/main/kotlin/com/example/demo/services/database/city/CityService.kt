package com.example.demo.services.database.city

import com.example.demo.models.City
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.city.exceptions.CityNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
interface CityService {
    fun findByGuid(guid: UUID): City?

    fun create(name: String): City?

    @Throws(CityNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): City?
}