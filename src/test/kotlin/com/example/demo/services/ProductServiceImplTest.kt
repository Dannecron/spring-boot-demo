package com.example.demo.services

import com.example.demo.BaseFeatureTest
import com.example.demo.models.Product
import com.example.demo.provider.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import kotlin.test.*

@ContextConfiguration(classes = [ProductRepository::class, ProductServiceImpl::class])
class ProductServiceImplTest: BaseFeatureTest() {
    @Autowired
    private lateinit var productService: ProductServiceImpl
    @Autowired
    private lateinit var productRepository: ProductRepository

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
        } finally {
            val id = product?.id
            if (id != null) {
                productRepository.deleteById(id)
            }

        }
    }
}