package com.github.dannecron.demo.core.services.order

import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.Order
import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.order.OrderEntity
import com.github.dannecron.demo.db.repository.OrderRepository
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

class OrderServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val orderRepository: OrderRepository = mock()
    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val orderService = OrderServiceImpl(
        orderRepository = orderRepository,
        commonGenerator = commonGenerator,
    )

    private val customerId = 123L
    private val orderEntity = OrderEntity(
        id = 22,
        guid = mockGuid,
        customerId = customerId,
        deliveredAt = null,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )
    private val order = Order(
        id = 22,
        guid = mockGuid,
        customerId = customerId,
        deliveredAt = null,
        createdAt = mockCurrentTime,
        updatedAt = null,
    )

    private val customer = Customer(
        id = customerId,
        guid = UUID.randomUUID(),
        name = "name",
        cityId = null,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
    )

    @Test
    fun `findByCustomerId - success`() {
        whenever(orderRepository.findByCustomerId(any())).thenReturn(listOf(orderEntity))

        val result = orderService.findByCustomerId(customerId)

        assertEquals(listOf(order), result)

        verify(orderRepository, times(1)).findByCustomerId(customerId)
    }

    @Test
    fun `create - success`() {
        val orderEntityForCreation = orderEntity.copy(id = null)

        whenever(orderRepository.save(any<OrderEntity>())).thenReturn(orderEntity)

        val result = orderService.create(customer)

        assertEquals(order, result)

        verify(orderRepository, times(1)).save(orderEntityForCreation)
    }
}
