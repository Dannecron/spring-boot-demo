package com.example.demo.config

import com.example.demo.services.kafka.dto.CityCreateDto
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper

@Configuration
class KafkaConsumerConfig(
    @Value("\${kafka.bootstrap-servers}")
    val servers: String,
    @Value("\${kafka.consumer.group-id}")
    val consumerGroup: String,
) {
//    @Bean
//    fun consumer(@Autowired cityService: CityService): Consumer = Consumer(
//        cityService = cityService,
//    )

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val configs = HashMap<String, Any>()
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        configs[ConsumerConfig.GROUP_ID_CONFIG] = consumerGroup
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        return DefaultKafkaConsumerFactory(configs)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        factory.setRecordMessageConverter(recordMessageConverter())

        return factory
    }

    @Bean
    fun recordMessageConverter(): RecordMessageConverter {
        val converter = StringJsonMessageConverter()

        val typeMapper = DefaultJackson2JavaTypeMapper()
        typeMapper.typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
        typeMapper.addTrustedPackages("com.baeldung.spring.kafka")

        val mappings: MutableMap<String, Class<*>> = HashMap()
        mappings["city"] = CityCreateDto::class.java
        typeMapper.idClassMapping = mappings

        converter.typeMapper = typeMapper

        return converter
    }
}