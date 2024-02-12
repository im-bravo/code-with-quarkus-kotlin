package org.bravo.survey.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.generator.EventTypeSets.INSERT_ONLY
import org.hibernate.type.SqlTypes
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractJavaType
import ulid.ULID
import java.time.Instant
import java.util.*

class UlidJavaType : AbstractJavaType<ULID>(ULID::class.java) {
    override fun <X : Any?> unwrap(value: ULID?, type: Class<X>?, options: WrapperOptions?): X {
        return value?.let {
            value.toBytes() as X
        } ?: null as X
    }

    override fun <X : Any?> wrap(value: X, options: WrapperOptions?): ULID? {
        if ( value == null) {
            return null
        }
        if (ByteArray::class.isInstance(value)) {
            return ULID.fromBytes(value as ByteArray)
        }
        throw unknownWrap(value!!::class.java)
    }

}

class CustomIdentifierGenerator : BeforeExecutionGenerator{
    override fun getEventTypes(): EnumSet<EventType> {
        return INSERT_ONLY
    }

    override fun generate(
        session: SharedSessionContractImplementor?,
        owner: Any?,
        currentValue: Any?,
        eventType: EventType?
    ): Any {
        return ULID.nextULID()
    }
}

@Converter
class UlidConverter : AttributeConverter<ULID, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ULID?): ByteArray? = attribute?.toBytes()
    override fun convertToEntityAttribute(dbData: ByteArray?): ULID? = dbData?.let(ULID::fromBytes)
}



class InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}

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


@Entity(name = "survey")
@Serializable
class Survey(

    val title: String,
    val description: String? = null,

) {
    constructor() : this("", null)

    @Id
    @JavaType(value = UlidJavaType::class)
    @JdbcTypeCode(SqlTypes.BINARY)
    @GeneratedValue(generator = "ulid_generator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.CustomIdentifierGenerator")
    @Column(name = "id", insertable = true)
    @Convert(converter = UlidConverter::class)
    @Serializable(with = UlidSerializer::class)
    val id: ULID? = null

    @Serializable(with = InstantSerializer::class)
    @Column(name = "created_at", insertable = true, updatable = false)
    val createdAt: Instant = Instant.now()
}
