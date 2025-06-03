package com.github.dannecron.demo.edgeintegration.configuration

import com.github.dannecron.demo.edgeintegration.client.neko.Client
import com.github.dannecron.demo.edgeintegration.client.neko.ClientImpl
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IntegrationConfiguration {

    @Bean
    fun httpClientEngine(): HttpClientEngine = CIO.create()

    @Bean
    fun nekoClient(
        httpClientEngine: HttpClientEngine,
        @Value("\${neko.baseUrl}") baseUrl: String,
    ): Client = ClientImpl(
        engine = httpClientEngine,
        baseUrl = baseUrl,
    )
}
