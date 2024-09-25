package com.example.demo

import com.example.demo.provider.MockedShopProvider
import com.example.demo.provider.ShopProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun shopProvider(): ShopProvider{
        return MockedShopProvider()
    }
}
