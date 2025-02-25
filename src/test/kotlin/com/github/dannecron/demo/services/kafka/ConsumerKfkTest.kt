package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.models.City
import com.github.dannecron.demo.services.database.city.CityService
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mockito.kotlin.after
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@ActiveProfiles("kafka")
@SpringBootTest
@EmbeddedKafka(
    brokerProperties = ["listeners=PLAINTEXT://localhost:3392", "port=3392"],
    topics = ["demo-city-sync"],
    partitions = 1,
)
@DirtiesContext
class ConsumerKfkTest {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    @Autowired
    private lateinit var metricRegistry: MeterRegistry

    @MockBean
    private lateinit var cityService: CityService

    @Test
    fun consumer_handleCityCreate() {
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

        whenever(cityService.create(any<CityCreateDto>())) doReturn City(
            id = 123,
            guid = cityGuid,
            name = cityName,
            createdAt = createdAt,
            updatedAt = null,
            deletedAt = null,
        )

        val message: Message<String> = MessageBuilder
            .withPayload(
                Json.encodeToString(cityCreateDto)
            )
            .setHeader(KafkaHeaders.TOPIC, "demo-city-sync")
            .build()

        kafkaTemplate.send(message)

        verify(cityService, after(1000).times(1)).create(cityCreateDto)

        assertEquals(1.0, metricRegistry.get("kafka_consumer_city_create").counter().count())
    }
}
