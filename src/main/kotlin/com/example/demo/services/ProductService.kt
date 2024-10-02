package com.example.demo.services

import com.example.demo.http.exceptions.NotFoundException
import com.example.demo.http.exceptions.UnprocessableException
import com.example.demo.models.Product
import com.example.demo.services.kafka.exceptions.InvalidArgumentException
import org.springframework.stereotype.Service
import java.util.*

@Service
interface ProductService {
    fun findByGuid(guid: UUID): Product?

    fun create(name: String, price: Long, description: String?): Product

    @Throws(NotFoundException::class, UnprocessableException::class)
    fun delete(guid: UUID): Product?

    @Throws(NotFoundException::class, InvalidArgumentException::class)
    fun syncToKafka(guid: UUID, topic: String?)
}