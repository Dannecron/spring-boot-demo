package com.github.dannecron.demo.edgeconsuming.config

import com.github.dannecron.demo.edgeconsuming.consumer.CityCreateConsumer
import com.github.dannecron.demo.edgeconsuming.dto.CityCreateDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class CityConsumerConfig {

    @Bean
    fun citySyncConsumer(cityCreateConsumer: CityCreateConsumer): Consumer<CityCreateDto> =
        Consumer(cityCreateConsumer::process)
}
