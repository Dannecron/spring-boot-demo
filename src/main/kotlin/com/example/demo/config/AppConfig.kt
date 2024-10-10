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
import com.example.demo.services.validation.SchemaValidator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class AppConfig(
    @Autowired private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModules(JavaTimeModule())
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper
    }

    @Bean
    fun shopProvider(): ShopProvider = MockedShopProvider()

    @Bean
    fun productService(
        @Autowired productRepository: ProductRepository,
        @Autowired producer: Producer,
    ): ProductService = ProductServiceImpl(
        kafkaProperties.producer.product.defaultSyncTopic,
        productRepository,
        producer,
    )

    @Bean
    fun cityService(@Autowired cityRepository: CityRepository): CityService = CityServiceImpl(cityRepository)

    @Bean
    fun schemaValidator(): SchemaValidator = SchemaValidator(kafkaProperties.validation.schema)

    @Bean
    fun otlpHttpSpanExporter(@Value("\${tracing.url}") url: String): OtlpHttpSpanExporter {
        return OtlpHttpSpanExporter.builder()
            .setEndpoint(url)
            .build()
    }

    @Bean
    fun observedAspect(@Autowired observationRegistry: ObservationRegistry): ObservedAspect {
        return ObservedAspect(observationRegistry)
    }
}

