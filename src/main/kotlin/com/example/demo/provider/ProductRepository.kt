package com.example.demo.provider

import com.example.demo.models.Product
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository: CrudRepository<Product, Long> {
    @Query(value = "SELECT * FROM Product WHERE guid = :guid")
    fun findByGuid(guid: UUID): Product?
}