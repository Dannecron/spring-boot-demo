package com.example.demo.services.database.product

import com.example.demo.models.Product
import com.example.demo.providers.ProductRepository
import com.example.demo.services.database.exceptions.AlreadyDeletedException
import com.example.demo.services.database.exceptions.ProductNotFoundException
import com.example.demo.services.kafka.Producer
import com.example.demo.utils.LoggerDelegate
import net.logstash.logback.marker.Markers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.OffsetDateTime
import java.util.*

class ProductServiceImpl(
    private val defaultSyncTopic: String,
    private val productRepository: ProductRepository,
    private val producer: Producer,
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
            guid = UUID.randomUUID(),
            name = name,
            description = description,
            price = price,
            createdAt = OffsetDateTime.now(),
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
            deletedAt = OffsetDateTime.now(),
        )

        return productRepository.save(deletedProduct)
    }

    override fun syncToKafka(guid: UUID, topic: String?) {
        val product = findByGuid(guid) ?: throw ProductNotFoundException()

        producer.produceProductInfo(topic ?: defaultSyncTopic, product)
    }

}
