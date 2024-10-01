package com.example.demo

import com.example.demo.provider.CityRepository
import com.example.demo.provider.MockedShopProvider
import com.example.demo.provider.ProductRepository
import com.example.demo.provider.ShopProvider
import com.example.demo.services.CityService
import com.example.demo.services.CityServiceImpl
import com.example.demo.services.ProductService
import com.example.demo.services.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun shopProvider(): ShopProvider{
        return MockedShopProvider()
    }

    @Bean
    fun productService(@Autowired productRepository: ProductRepository): ProductService = ProductServiceImpl(productRepository)

    @Bean
    fun cityService(@Autowired cityRepository: CityRepository): CityService = CityServiceImpl(cityRepository)
}
