package com.github.dannecron.demo.db.entity.order

import com.github.dannecron.demo.db.serialialization.OffsetDateTimeSerialization
import com.github.dannecron.demo.db.serialialization.UuidSerialization
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "order_product")
@Serializable
data class OrderProduct(
    @Id
    @Serializable(with = UuidSerialization::class)
    val guid: UUID,
    @Column(value = "order_id")
    val orderId: Long,
    @Column(value = "product_id")
    val productId: Long,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerialization::class)
    @Column(value = "updated_at")
    val updatedAt: OffsetDateTime?,
): Persistable<UUID> {
    @Transient
    var isNewInstance: Boolean? = null

    override fun getId(): UUID {
        return guid
    }

    override fun isNew(): Boolean {
        return isNewInstance ?: true
    }
}
