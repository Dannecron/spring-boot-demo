package com.github.dannecron.demo.edgeconsuming.consumer

import com.github.dannecron.demo.edgeconsuming.dto.CityCreateDto

interface CityCreateConsumer {
    fun process(cityCreateDto: CityCreateDto)
}
