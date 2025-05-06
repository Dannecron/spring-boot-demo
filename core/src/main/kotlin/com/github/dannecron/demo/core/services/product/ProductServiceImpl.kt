package com.github.dannecron.demo.core.services.product

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.core.utils.LoggerDelegate
import com.github.dannecron.demo.db.entity.ProductEntity
import com.github.dannecron.demo.db.repository.ProductRepository
import net.logstash.logback.marker.Markers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val commonGenerator: CommonGenerator,
): ProductService {
    private val logger by LoggerDelegate()

    override fun findByGuid(guid: UUID): Product? = productRepository.findByGuid(guid)?.toCore()
        .also {
            logger.debug(
                Markers.appendEntries(mapOf("guid" to guid, "idResult" to it?.id)),
                "find product by guid",
            )
        }

    override fun findAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)
        .map { it.toCore() }

    override fun create(name: String, price: Long, description: String?): Product =
        ProductEntity(
            id = null,
            guid = commonGenerator.generateUUID(),
            name = name,
            description = description,
            price = price,
            createdAt = commonGenerator.generateCurrentTime(),
            updatedAt = null,
            deletedAt = null,
        ).let(productRepository::save).toCore()

    @Throws(ProductNotFoundException::class, AlreadyDeletedException::class)
    override fun delete(guid: UUID): Product {
        val product = findByGuid(guid) ?: throw ProductNotFoundException()

        if (product.isDeleted()) {
            throw AlreadyDeletedException()
        }

        return product.copy(
            deletedAt = commonGenerator.generateCurrentTime(),
        ).toEntity()
            .let(productRepository::save)
            .toCore()
    }

    private fun ProductEntity.toCore() = Product(
        id = id!!,
        guid = guid,
        name = name,
        description = description,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )

    private fun Product.toEntity() = ProductEntity(
        id = id,
        guid = guid,
        name = name,
        description = description,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
}
