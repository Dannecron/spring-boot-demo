package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.BaseDbTest
import com.github.dannecron.demo.db.entity.ProductEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ContextConfiguration(classes = [ProductRepository::class])
class ProductEntityRepositoryTest : BaseDbTest() {

    @Autowired
    private lateinit var productRepository: ProductRepository

    private val productGuid = UUID.fromString("1fb5c7e4-8ce2-43b8-8ca7-1089b04959b9")
    private val productEntity = ProductEntity(
        id = 1000,
        guid = productGuid,
        name = "product",
        description = "description",
        price = 10000,
        createdAt = OffsetDateTime.parse("2025-01-01T12:10:05+00:00"),
        updatedAt = null,
        deletedAt = null,
    )

    @Test
    @Sql(scripts = ["/sql/insert_product.sql"])
    fun findByGuid() {
        val result = productRepository.findByGuid(productGuid)
        assertEquals(productEntity, result)

        val emptyResult = productRepository.findByGuid(UUID.randomUUID())
        assertNull(emptyResult)
    }
}
