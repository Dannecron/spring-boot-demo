package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.BaseDbTest
import com.github.dannecron.demo.models.Customer
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.models.order.Order
import com.github.dannecron.demo.providers.CustomerRepository
import com.github.dannecron.demo.providers.OrderRepository
import com.github.dannecron.demo.providers.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ContextConfiguration(classes = [OrderServiceImpl::class])
class OrderServiceImplTest: BaseDbTest() {
    @Autowired
    private lateinit var orderRepository: OrderRepository
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var customerRepository: CustomerRepository
    @Autowired
    private lateinit var orderServiceImpl: OrderServiceImpl

    @Test
    fun createAndFind_success() {
        var productOneId: Long? = null
        var productTwoId: Long? = null
        var customerId: Long? = null
        var order: Order? = null

        try {
            val productOne = makeProduct().let { productRepository.save(it) }.also { productOneId = it.id!! }
            val productTwo = makeProduct(price = 20000L)
                .let { productRepository.save(it) }
                .also { productTwoId = it.id!! }
            val customer = makeCustomer().let { customerRepository.save(it) }.also { customerId = it.id!! }

            order = orderServiceImpl.createOrder(customer, setOf(productOne, productTwo))
            assertNotNull(order.id)

            val orderWithProducts = orderServiceImpl.getByCustomerId(customerId = customerId!!)
            assertEquals(1, orderWithProducts.count())
            orderWithProducts.first().also {
                assertEquals(order.id, it.order.id)
                assertEquals(2, it.products.count())
                assertEquals(productTwo.id, it.getMostExpensiveOrderedProduct()?.id)
                assertEquals(300.00, it.getTotalOrderPrice())
            }


        } finally {
            order?.id?.also { orderRepository.deleteById(it) }
            customerId?.also { customerRepository.deleteById(it) }
            productOneId?.also { productRepository.deleteById(it) }
            productTwoId?.also { productRepository.deleteById(it) }
        }
    }

    private fun makeProduct(price: Long = 10000L): Product = Product(
        id = null,
        guid = UUID.randomUUID(),
        name = "name" + UUID.randomUUID(),
        description = null,
        price = price,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )

    private fun makeCustomer(): Customer = Customer(
        id = null,
        guid = UUID.randomUUID(),
        name = "client",
        cityId = null,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
    )
}
