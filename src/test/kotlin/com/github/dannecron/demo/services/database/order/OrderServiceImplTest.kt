package com.github.dannecron.demo.services.database.order

import com.github.dannecron.demo.BaseDbTest
import com.github.dannecron.demo.models.Customer
import com.github.dannecron.demo.models.Order
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.providers.CustomerRepository
import com.github.dannecron.demo.providers.OrderRepository
import com.github.dannecron.demo.providers.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test
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
    fun create_success() {
        var productOne: Product? = null
        var productTwo: Product? = null
        var customer: Customer? = null
        var order: Order? = null

        try {
            productOne = makeProduct().let { productRepository.save(it) }
            productTwo = makeProduct().let { productRepository.save(it) }
            customer = makeCustomer().let { customerRepository.save(it) }

            order = orderServiceImpl.createOrder(customer, setOf(productOne, productTwo))
            assertNotNull(order.id)
        } finally {
            order?.id?.also { orderRepository.deleteById(it) }
            customer?.id?.also { customerRepository.deleteById(it) }
            productOne?.id?.also { productRepository.deleteById(it) }
            productTwo?.id?.also { productRepository.deleteById(it) }
        }
    }

    private fun makeProduct(): Product = Product(
        id = null,
        guid = UUID.randomUUID(),
        name = "name" + UUID.randomUUID(),
        description = null,
        price = 10000,
        createdAt = OffsetDateTime.now(),
        updatedAt = null,
        deletedAt = null,
    )

    private fun makeCustomer(): Customer = Customer(
        id = null,
        guid = UUID.randomUUID(),
        name = "client",
        cityId = null,
        createdAt =OffsetDateTime.now(),
        updatedAt = null,
    )
}
