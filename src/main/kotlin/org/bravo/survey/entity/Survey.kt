package org.bravo.survey.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.generator.EventTypeSets.INSERT_ONLY
import org.hibernate.reactive.id.ReactiveIdentifierGenerator
import org.hibernate.reactive.session.ReactiveConnectionSupplier
import org.hibernate.type.SqlTypes
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractJavaType
import ulid.ULID
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage


class UlidGenerator: ReactiveIdentifierGenerator<ULID> {
    override fun generate(session: ReactiveConnectionSupplier?, entity: Any?): CompletionStage<ULID> {
        return CompletableFuture.completedStage(ULID.nextULID())
    }
}

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


@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Instant::class)
class InstantSerializer : KSerializer<Instant> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.from(formatter.parse(decoder.decodeString()))
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
    val id: ULID? = null

    @Serializable(with = InstantSerializer::class)
    @Column(name = "created_at", insertable = true, updatable = false)
    val createdAt: Instant? = null
}
