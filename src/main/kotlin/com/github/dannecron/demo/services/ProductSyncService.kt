package com.github.dannecron.demo.services


import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.services.kafka.exceptions.InvalidArgumentException
import java.util.UUID

interface ProductSyncService {

    @Throws(ProductNotFoundException::class, InvalidArgumentException::class)
    fun syncToKafka(guid: UUID, topic: String?)
}
