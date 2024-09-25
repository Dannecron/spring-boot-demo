package com.example.demo

import com.example.demo.provider.MockedShopProvider
import com.example.demo.provider.ShopProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.example.demo.providers"])
class AppConfig {
    @Bean
    fun shopProvider(): ShopProvider{
        return MockedShopProvider()
    }
}
