package com.github.dannecron.demo.edgeconsumer.config

import com.github.dannecron.demo.edgeconsuming.config.CityConsumerConfig
import com.github.dannecron.demo.edgeconsuming.consumer.CityCreateConsumer
import com.github.dannecron.demo.edgeconsuming.dto.CityCreateDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.mockito.kotlin.after
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@SpringJUnitConfig(
    classes = [CityConsumerConfig::class, TestChannelBinderConfiguration::class],
)
@TestPropertySource(
    properties = [
        "spring.jmx.enabled=false",
        "spring.cloud.stream.default-binder=kafka",
        "spring.cloud.function.definition=citySyncConsumer",
        "spring.cloud.stream.bindings.citySyncConsumer-in-0.destination=demo-city-sync"
    ],
)
@EnableAutoConfiguration(
    exclude = [
        WebMvcAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        SecurityAutoConfiguration::class,
    ]
)
class CityConsumerConfigTest {

    @Autowired
    private lateinit var inputDestination: InputDestination

    @MockBean
    private lateinit var cityCreateConsumer: CityCreateConsumer

    @Test
    fun `citySyncConsumer - success`() {
        val cityGuid = UUID.randomUUID()
        val cityName = "new-city"
        val createdAt = OffsetDateTime.now().minusDays(1)
        val cityCreateDto = CityCreateDto(
            guid = cityGuid.toString(),
            name = cityName,
            createdAt = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            updatedAt = null,
            deletedAt = null,
        )

        val rawEvent = Json.encodeToString(cityCreateDto)
        val msg = MessageBuilder.withPayload(rawEvent).build()
        inputDestination.send(msg, "demo-city-sync")

        verify(cityCreateConsumer, after(1000).times(1)).process(cityCreateDto)
    }
}
