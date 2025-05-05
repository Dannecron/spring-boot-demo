package com.github.dannecron.demo.services.database.product

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.Product
import com.github.dannecron.demo.db.repository.ProductRepository
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.ProductNotFoundException
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException
import com.github.dannecron.demo.utils.LoggerDelegate
import net.logstash.logback.marker.Markers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val producer: Producer,
    private val commonGenerator: CommonGenerator,
): ProductService {
    private val logger by LoggerDelegate()

    override fun findByGuid(guid: UUID): Product? = productRepository.findByGuid(guid)
        .also {
            logger.debug(
                Markers.appendEntries(mapOf("guid" to guid, "idResult" to it?.id)),
                "find product by guid",
            )
        }

    override fun findAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

    override fun create(name: String, price: Long, description: String?): Product {
        val product = Product(
            id = null,
            guid = commonGenerator.generateUUID(),
            name = name,
            description = description,
            price = price,
            createdAt = commonGenerator.generateCurrentTime(),
            updatedAt = null,
            deletedAt = null,
        )

        return productRepository.save(product)
    }

    override fun delete(guid: UUID): Product {
        val product = findByGuid(guid) ?: throw ProductNotFoundException()

        if (product.isDeleted()) {
            throw AlreadyDeletedException()
        }

        val deletedProduct = product.copy(
            deletedAt = commonGenerator.generateCurrentTime(),
        )

        return productRepository.save(deletedProduct)
    }

    override fun syncToKafka(guid: UUID, topic: String?) {
        val product = findByGuid(guid) ?: throw ProductNotFoundException()

        producer.produceProductSync(product.toKafkaDto())
    }

    private fun Product.toKafkaDto() = ProductDto(
        id = id ?: throw InvalidArgumentException("product.id"),
        guid = guid.toString(),
        name = name,
        description = description,
        price = price,
        createdAt = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = updatedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        deletedAt = deletedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    )
}
