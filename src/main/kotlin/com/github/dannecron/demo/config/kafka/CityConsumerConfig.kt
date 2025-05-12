package com.github.dannecron.demo.config.kafka

import com.github.dannecron.demo.services.kafka.CityCreateConsumer
import com.github.dannecron.demo.services.kafka.dto.CityCreateDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class CityConsumerConfig {

    @Bean
    fun citySyncConsumer(cityCreateConsumer: CityCreateConsumer): Consumer<CityCreateDto> =
        Consumer(cityCreateConsumer::process)
}
