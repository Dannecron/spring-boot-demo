package com.github.dannecron.demo.services

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.core.services.product.ProductService
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.kafka.dto.ProductDto
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class ProductSyncServiceImpl(
    private val productService: ProductService,
    private val producer: Producer,
) : ProductSyncService {

    @Throws(ProductNotFoundException::class, InvalidArgumentException::class)
    override fun syncToKafka(guid: UUID, topic: String?) {
        val product = productService.findByGuid(guid) ?: throw ProductNotFoundException()

        producer.produceProductSync(product.toKafkaDto())
    }

    private fun Product.toKafkaDto() = ProductDto(
        id = id,
        guid = guid.toString(),
        name = name,
        description = description,
        price = price,
        createdAt = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        updatedAt = updatedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        deletedAt = deletedAt?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    )
}
