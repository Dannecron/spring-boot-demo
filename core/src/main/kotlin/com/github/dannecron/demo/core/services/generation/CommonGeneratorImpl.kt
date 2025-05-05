package com.github.dannecron.demo.core.services.generation

import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.UUID

@Component
class CommonGeneratorImpl : CommonGenerator {
    override fun generateUUID(): UUID {
        return UUID.randomUUID()
    }

    override fun generateCurrentTime(): OffsetDateTime {
        return OffsetDateTime.now()
    }
}
