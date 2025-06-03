package com.github.dannecron.demo.edgeproducing.producer

import com.github.dannecron.demo.edgecontracts.validation.SchemaValidator
import com.github.dannecron.demo.edgeproducing.dto.ProductDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class ProductProducerImpl(
    private val streamBridge: StreamBridge,
    private val schemaValidator: SchemaValidator,
): ProductProducer {
    private companion object {
        private const val BINDING_NAME_PRODUCT_SYNC = "productSyncProducer"
        private const val SCHEMA_KAFKA_PRODUCT_SYNC = "kafka-product-sync"
    }

    override fun produceProductSync(product: ProductDto) {
        Json.encodeToJsonElement((product))
            .also { schemaValidator.validate(SCHEMA_KAFKA_PRODUCT_SYNC, it) }
            .let {
                MessageBuilder.withPayload(it.toString())
                    .setHeader("X-Custom-Header", "some-custom-header")
                    .build()
            }
            .let {
                streamBridge.send(
                    BINDING_NAME_PRODUCT_SYNC,
                    it,
                )
            }
    }
}
