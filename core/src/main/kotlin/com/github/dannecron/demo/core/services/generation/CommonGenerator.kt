package com.github.dannecron.demo.core.services.generation

import java.time.OffsetDateTime
import java.util.UUID

interface CommonGenerator {

    fun generateUUID(): UUID

    fun generateCurrentTime(): OffsetDateTime
}
