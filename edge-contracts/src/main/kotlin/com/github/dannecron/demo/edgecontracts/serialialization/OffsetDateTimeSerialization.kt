package com.github.dannecron.demo.db.serialialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object OffsetDateTimeSerialization: KSerializer<OffsetDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("Time", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): OffsetDateTime = OffsetDateTime.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }
}
