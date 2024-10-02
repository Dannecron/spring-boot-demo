package com.example.demo.config

import com.example.demo.providers.CityRepository
import com.example.demo.providers.MockedShopProvider
import com.example.demo.providers.ProductRepository
import com.example.demo.providers.ShopProvider
import com.example.demo.services.database.city.CityService
import com.example.demo.services.database.city.CityServiceImpl
import com.example.demo.services.database.product.ProductService
import com.example.demo.services.database.product.ProductServiceImpl
import com.example.demo.services.kafka.Producer
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
    @Value("\${kafka.producer.product.default-sync-topic}")
    private val defaultProductSyncTopic: String
) {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()

    @Bean
    fun shopProvider(): ShopProvider = MockedShopProvider()

    @Bean
    fun productService(
        @Autowired productRepository: ProductRepository,
        @Autowired producer: Producer,
    ): ProductService = ProductServiceImpl(
        defaultProductSyncTopic,
        productRepository,
        producer,
    )

    @Bean
    fun cityService(@Autowired cityRepository: CityRepository): CityService = CityServiceImpl(cityRepository)
}
