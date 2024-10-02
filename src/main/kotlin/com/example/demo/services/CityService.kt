package com.example.demo.services

import com.example.demo.http.exceptions.NotFoundException
import com.example.demo.http.exceptions.UnprocessableException
import com.example.demo.models.City
import org.springframework.stereotype.Service
import java.util.*

@Service
interface CityService {
    fun findByGuid(guid: UUID): City?

    fun create(name: String): City?

    @Throws(NotFoundException::class, UnprocessableException::class)
    fun delete(guid: UUID): City?
}