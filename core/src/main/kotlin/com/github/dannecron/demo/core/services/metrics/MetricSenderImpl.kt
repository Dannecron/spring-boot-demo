package com.github.dannecron.demo.core.services.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class MetricsSenderImpl(
    metricRegistry: MeterRegistry,
) : MetricsSender {
    private val consumerCityCreateCounter = Counter.builder("kafka_consumer_city_create")
        .description("consumed created city event")
        .register(metricRegistry)

    override fun incrementConsumerCityCreate() {
        consumerCityCreateCounter.increment()
    }
}
