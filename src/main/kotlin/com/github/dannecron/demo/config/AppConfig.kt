package com.github.dannecron.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.dannecron.demo.config.properties.KafkaProperties
import com.github.dannecron.demo.config.properties.ValidationProperties
import com.github.dannecron.demo.providers.CityRepository
import com.github.dannecron.demo.providers.CustomerRepository
import com.github.dannecron.demo.providers.ProductRepository
import com.github.dannecron.demo.services.database.city.CityService
import com.github.dannecron.demo.services.database.city.CityServiceImpl
import com.github.dannecron.demo.services.database.customer.CustomerService
import com.github.dannecron.demo.services.database.customer.CustomerServiceImpl
import com.github.dannecron.demo.services.database.product.ProductService
import com.github.dannecron.demo.services.database.product.ProductServiceImpl
import com.github.dannecron.demo.services.kafka.Producer
import com.github.dannecron.demo.services.validation.SchemaValidator
import com.github.dannecron.demo.services.validation.SchemaValidatorImp
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.github.dannecron.demo.services.neko.Client as NekoClient
import com.github.dannecron.demo.services.neko.ClientImpl as NekoClientImpl

@Configuration
@EnableConfigurationProperties(KafkaProperties::class, ValidationProperties::class)
class AppConfig(
    @Autowired private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        registerModules(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

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
    fun customerService(
        @Autowired customerRepository: CustomerRepository,
        @Autowired cityRepository: CityRepository,
    ): CustomerService = CustomerServiceImpl(customerRepository, cityRepository)

    @Bean
    fun schemaValidator(
        @Autowired validationProperties: ValidationProperties,
    ): SchemaValidator = SchemaValidatorImp(validationProperties.schema)

    @Bean
    fun otlpHttpSpanExporter(@Value("\${tracing.url}") url: String) = OtlpHttpSpanExporter.builder()
        .setEndpoint(url)
        .build()

    @Bean
    fun observedAspect(@Autowired observationRegistry: ObservationRegistry) = ObservedAspect(observationRegistry)

    @Bean
    fun httpClientEngine(): HttpClientEngine = CIO.create()

    @Bean
    fun nekoClient(
        @Autowired httpClientEngine: HttpClientEngine,
        @Value("\${neko.baseUrl}") baseUrl: String,
    ): NekoClient = NekoClientImpl(
        engine = httpClientEngine,
        baseUrl = baseUrl,
    )
}

