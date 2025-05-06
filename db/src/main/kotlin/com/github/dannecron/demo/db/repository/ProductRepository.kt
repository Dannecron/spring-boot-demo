package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.ProductEntity
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
interface ProductRepository: CrudRepository<ProductEntity, Long>, PagingAndSortingRepository<ProductEntity, Long> {
    fun findByGuid(guid: UUID): ProductEntity?

    @Query(value = "UPDATE Product SET deleted_at = :deletedAt WHERE guid = :guid RETURNING *")
    fun softDelete(guid: UUID, deletedAt: OffsetDateTime): ProductEntity?
}
