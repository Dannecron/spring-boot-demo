package com.example.demo.providers

import com.example.demo.models.Product
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface ProductRepository: CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    fun findByGuid(guid: UUID): Product?

    @Query(value = "UPDATE Product SET deleted_at = :deletedAt WHERE guid = :guid RETURNING *")
    fun softDelete(@Param("guid") guid: UUID, @Param("deletedAt") deletedAt: OffsetDateTime): Product?
}