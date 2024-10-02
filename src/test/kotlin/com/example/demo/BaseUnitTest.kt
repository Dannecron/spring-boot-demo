package com.example.demo

import com.example.demo.services.kafka.Consumer
import org.springframework.boot.test.mock.mockito.MockBean

open class BaseUnitTest {
    @MockBean
    lateinit var consumer: Consumer
}