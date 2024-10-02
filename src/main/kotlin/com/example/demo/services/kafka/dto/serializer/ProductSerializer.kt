package com.example.demo.services.kafka.dto.serializer

import com.example.demo.services.kafka.dto.ProductDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import javax.sql.rowset.serial.SerialException

class ProductSerializer: Serializer<ProductDto> {
    private val objectMapper = ObjectMapper()

    override fun serialize(topic: String?, data: ProductDto?): ByteArray {
        return objectMapper.writeValueAsBytes(
            data ?: throw SerialException()
        )
    }

    override fun close() {
        // no logic
    }
}