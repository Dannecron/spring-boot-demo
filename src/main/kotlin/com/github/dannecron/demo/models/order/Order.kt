package com.github.dannecron.demo.models.order

import com.github.dannecron.demo.services.serializables.OffsetDateTimeSerialization
import com.github.dannecron.demo.services.serializables.UuidSerialization
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table(value = "order")
@Serializable
data class Order(
    @Id
    val id: Long?,
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    val customerId: Long,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "delivered_at")
    val deliveredAt: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?
) {
    fun isDelivered(): Boolean = deliveredAt != null
}
