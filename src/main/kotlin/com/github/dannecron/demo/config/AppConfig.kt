package com.github.dannecron.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
}

