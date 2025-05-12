package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Order
import com.github.dannecron.demo.core.dto.OrderProduct
import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.order.OrderProductEntity
import com.github.dannecron.demo.db.repository.OrderProductRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.assertEquals

class OrderProductServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val orderProductRepository: OrderProductRepository = mock()
    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val orderProductService = OrderProductServiceImpl(
        orderProductRepository = orderProductRepository,
        commonGenerator = commonGenerator
    )

    private val orderId = 22L
    private val productId = 12L
    private val order = Order(
        id = orderId,
        guid = UUID.randomUUID(),
        customerId = 44L,
        deliveredAt = null,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
    )
    private val product = Product(
        id = productId,
        guid = UUID.randomUUID(),
        name = "prod",
        description = "some",
        price = 10050,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null
    )
    private val orderProductEntity = OrderProductEntity(
        guid = mockGuid,
        orderId = orderId,
        productId = productId,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )
    private val orderProduct = OrderProduct(
        guid = mockGuid,
        orderId = orderId,
        productId = productId,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )

    @Test
    fun `create - success`() {
        whenever(orderProductRepository.save(any<OrderProductEntity>())).thenReturn(orderProductEntity)

        val result = orderProductService.create(order, product)

        assertEquals(orderProduct, result)

        verify(orderProductRepository, times(1)).save(orderProductEntity)
    }
}
