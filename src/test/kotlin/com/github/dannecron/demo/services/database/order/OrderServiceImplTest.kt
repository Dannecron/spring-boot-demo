package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.core.services.generation.CommonGenerator
import com.github.dannecron.demo.db.entity.Customer
import com.github.dannecron.demo.db.entity.Product
import com.github.dannecron.demo.db.entity.order.Order
import com.github.dannecron.demo.db.entity.order.OrderProduct
import com.github.dannecron.demo.db.repository.OrderProductRepository
import com.github.dannecron.demo.db.repository.OrderRepository
import com.github.dannecron.demo.db.repository.ProductRepository
import com.github.dannecron.demo.models.OrderWithProducts
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderServiceImplTest {
    private val mockGuid = UUID.randomUUID()
    private val mockCurrentTime = OffsetDateTime.now()

    private val commonGenerator: CommonGenerator = mock {
        on { generateUUID() } doReturn mockGuid
        on { generateCurrentTime() } doReturn mockCurrentTime
    }

    private val orderRepository: OrderRepository = mock()
    private val productRepository: ProductRepository = mock()
    private val orderProductRepository: OrderProductRepository = mock()

    private val orderServiceImpl = OrderServiceImpl(
        orderRepository = orderRepository,
        orderProductRepository = orderProductRepository,
        productRepository = productRepository,
        commonGenerator = commonGenerator,
    )

    private val now = OffsetDateTime.now()

    private val customerId = 123L
    private val customer = Customer(
        id = customerId,
        guid = UUID.randomUUID(),
        name = "customer",
        cityId = null,
        createdAt = now,
        updatedAt = null,
    )

    private val orderOneId = 1001L
    private val orderTwoId = 1002L
    private val orderOne = Order(
        id = orderOneId,
        guid = UUID.randomUUID(),
        customerId = customerId,
        deliveredAt = now.minusHours(1),
        createdAt = now.minusDays(1),
        updatedAt = now.minusHours(1),
    )
    private val orderTwo = Order(
        id = orderTwoId,
        guid = UUID.randomUUID(),
        customerId = customerId,
        deliveredAt = null,
        createdAt = now,
        updatedAt = null,
    )

    private val productId = 100L
    private val product = Product(
        id = productId,
        guid = UUID.randomUUID(),
        name = "product",
        description = null,
        price = 10000L,
        createdAt = now.minusMonths(1),
        updatedAt = null,
        deletedAt = null,
    )

    private val orderProduct = OrderProduct(
        guid = UUID.randomUUID(),
        orderId = orderOneId,
        productId = productId,
        createdAt = now.minusDays(1),
        updatedAt = null,
    )

    @Test
    fun findByCustomerId() {
        whenever(orderRepository.findByCustomerId(any())).thenReturn(listOf(orderOne, orderTwo))
        whenever(orderProductRepository.findByOrderId(any()))
            .thenReturn(listOf(orderProduct))
            .thenReturn(emptyList())
        whenever(productRepository.findAllById(any())).thenReturn(listOf(product))

        val expectedResult = listOf(
            OrderWithProducts(
                order = orderOne,
                products = listOf(product),
            ),
            OrderWithProducts(
                order = orderTwo,
                products = emptyList(),
            ),
        )

        val result = orderServiceImpl.findByCustomerId(customerId)
        assertEquals(expectedResult, result)

        verify(orderRepository, times(1)).findByCustomerId(customerId)
        verify(orderProductRepository, times(1)).findByOrderId(orderOneId)
        verify(orderProductRepository, times(1)).findByOrderId(orderTwoId)
        verify(productRepository, times(1)).findAllById(listOf(productId))
    }

    @Test
    fun create() {
        val newOrder = orderTwo.copy(
            guid = mockGuid,
            createdAt = mockCurrentTime,
        )
        val newOrderProduct = orderProduct.copy(
            guid = mockGuid,
            createdAt = mockCurrentTime,
            orderId = orderTwoId,
        )

        whenever(orderRepository.save(any<Order>())).thenReturn(newOrder)
        whenever(orderProductRepository.saveAll(any<List<OrderProduct>>())).thenReturn(listOf(newOrderProduct))

        val result = orderServiceImpl.createOrder(
            customer = customer,
            products = setOf(product),
        )

        assertEquals(newOrder, result)

        verify(orderRepository, times(1)).save(newOrder.copy(id = null))
        verify(orderProductRepository, times(1)).saveAll(listOf(newOrderProduct))
    }
}
