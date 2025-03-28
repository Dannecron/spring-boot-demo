package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.City
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
interface CityRepository: CrudRepository<City, Long> {
    fun findByGuid(guid: UUID): City?

    @Query(value = "UPDATE city SET deleted_at = :deletedAt WHERE guid = :guid RETURNING *")
    fun softDelete(@Param("guid") guid: UUID, @Param("deletedAt") deletedAt: OffsetDateTime): City?
}
