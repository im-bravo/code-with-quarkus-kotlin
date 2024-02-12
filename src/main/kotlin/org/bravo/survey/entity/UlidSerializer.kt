package org.bravo.survey.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ulid.ULID

class UlidSerializer : KSerializer<ULID> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ulid.ULID", PrimitiveKind.STRING)


    override fun deserialize(decoder: Decoder): ULID {
        return ULID.fromBytes(decoder.decodeString().toByteArray())
    }

    override fun serialize(encoder: Encoder, value: ULID) {
        return encoder.encodeString(value.toString())
    }
}