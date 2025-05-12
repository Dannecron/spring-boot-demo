package com.github.dannecron.demo.services.kafka

import com.github.dannecron.demo.services.kafka.dto.CityCreateDto

interface CityCreateConsumer {
    fun process(cityCreateDto: CityCreateDto)
}
