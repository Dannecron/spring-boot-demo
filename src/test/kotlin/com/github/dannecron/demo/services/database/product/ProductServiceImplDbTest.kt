package com.github.dannecron.demo.services.database.product

import com.github.dannecron.demo.BaseDbTest
import com.github.dannecron.demo.models.Product
import com.github.dannecron.demo.providers.ProductRepository
import com.github.dannecron.demo.services.database.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.services.database.exceptions.ProductNotFoundException
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import java.util.*
import kotlin.test.*

@ContextConfiguration(classes = [ProductRepository::class])
class ProductServiceImplDbTest: BaseDbTest() {
    private lateinit var productService: ProductServiceImpl
    @Autowired
    private lateinit var productRepository: ProductRepository

    @BeforeTest
    fun setUp() {
        productService = ProductServiceImpl(
            defaultSyncTopic = "some-default-topic",
            productRepository = productRepository,
            producer = producer
        )
    }

    @Test
    fun createFindDelete_success() {
        val name = "new-product-name"
        val price = 33333.toLong()
        val description = "some-description"
        var product: Product? = null

        try {
            product = productService.create(name = name, price = price, description = description)
            assertNotNull(product.id)
            assertEquals(name, product.name)
            assertEquals(price, product.price)
            assertEquals(333.33, product.getPriceDouble())

            val dbProduct = productService.findByGuid(product.guid)
            assertNotNull(dbProduct)
            assertEquals(product.id, dbProduct.id)
            assertFalse(dbProduct.isDeleted())

            val deletedProduct = productService.delete(product.guid)
            assertNotNull(deletedProduct)
            assertEquals(product.id, deletedProduct.id)
            assertNotNull(deletedProduct.deletedAt)
            assertTrue(deletedProduct.isDeleted())

            // try to delete already deleted product
            assertThrows<AlreadyDeletedException> {
                productService.delete(product.guid)
            }

            assertThrows<ProductNotFoundException> {
                productService.delete(UUID.randomUUID())
            }
        } finally {
            val id = product?.id
            if (id != null) {
                productRepository.deleteById(id)
            }
        }
    }
}
