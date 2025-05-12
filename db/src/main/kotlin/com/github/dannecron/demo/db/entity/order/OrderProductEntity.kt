package com.github.dannecron.demo.db.entity.order

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table(value = "order_product")
data class OrderProductEntity(
    @Id
    val guid: UUID,
    @Column(value = "order_id")
    val orderId: Long,
    @Column(value = "product_id")
    val productId: Long,
    @Column(value = "created_at")
    val createdAt: OffsetDateTime,
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
