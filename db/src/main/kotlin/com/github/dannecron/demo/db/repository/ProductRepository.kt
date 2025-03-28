package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.Product
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
interface ProductRepository: CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    fun findByGuid(guid: UUID): Product?

    @Query(value = "UPDATE Product SET deleted_at = :deletedAt WHERE guid = :guid RETURNING *")
    fun softDelete(guid: UUID, deletedAt: OffsetDateTime): Product?
}
