package com.example.demo.providers

import com.example.demo.models.City
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface CityRepository: CrudRepository<City, Long> {
    fun findByGuid(guid: UUID): City?

    @Query(value = "UPDATE City SET deleted_at = :deletedAt WHERE guid = :guid RETURNING *")
    fun softDelete(@Param("guid") guid: UUID, @Param("deletedAt") deletedAt: OffsetDateTime): City?
}