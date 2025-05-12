package com.github.dannecron.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.dannecron.demo.core.config.properties.ValidationProperties
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.github.dannecron.demo.services.neko.Client as NekoClient
import com.github.dannecron.demo.services.neko.ClientImpl as NekoClientImpl

@Configuration
@EnableConfigurationProperties(ValidationProperties::class)
class AppConfig {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        registerModules(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    @Bean
    fun otlpHttpSpanExporter(@Value("\${tracing.url}") url: String): OtlpHttpSpanExporter =
        OtlpHttpSpanExporter.builder()
            .setEndpoint(url)
            .build()

    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry) = ObservedAspect(observationRegistry)

    @Bean
    fun httpClientEngine(): HttpClientEngine = CIO.create()

    @Bean
    fun nekoClient(
        httpClientEngine: HttpClientEngine,
        @Value("\${neko.baseUrl}") baseUrl: String,
    ): NekoClient = NekoClientImpl(
        engine = httpClientEngine,
        baseUrl = baseUrl,
    )
}

