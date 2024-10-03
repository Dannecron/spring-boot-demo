package com.example.demo.config

import com.example.demo.services.database.city.CityService
import com.example.demo.services.kafka.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.bootstrap-servers}")
    val servers: String,
    @Value("\${kafka.consumer.group-id}")
    val consumerGroup: String,
) {
    @Bean
    fun consumer(
        @Autowired cityService: CityService,
    ): Consumer = Consumer(
        cityService = cityService,
    )

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configs = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to servers,
            ConsumerConfig.GROUP_ID_CONFIG to consumerGroup,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        )

        return DefaultKafkaConsumerFactory(configs)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()

        return factory
    }
}