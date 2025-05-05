package com.github.dannecron.demo.services.database.city

import com.github.dannecron.demo.db.entity.City
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import org.springframework.stereotype.Service
import java.util.*

@Service
interface CityService {
    fun findByGuid(guid: UUID): City?

    fun create(name: String): City
    fun create(kafkaCityDto: CityCreateDto): City

    @Throws(CityNotFoundException::class, AlreadyDeletedException::class)
    fun delete(guid: UUID): City
}
