package org.bravo.survey.entity

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.reactive.id.ReactiveIdentifierGenerator
import org.hibernate.reactive.session.ReactiveConnectionSupplier
import org.hibernate.type.SqlTypes
import ulid.ULID
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

class UlidGenerator: ReactiveIdentifierGenerator<ULID> {
    override fun generate(session: ReactiveConnectionSupplier?, entity: Any?): CompletionStage<ULID> {
        return CompletableFuture.completedStage(ULID.nextULID())
    }
}

@Converter(autoApply = true)
class UlidConverter : AttributeConverter<ULID, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ULID?): ByteArray? = attribute?.toBytes()

    override fun convertToEntityAttribute(dbData: ByteArray?): ULID? = dbData?.let { ULID.fromBytes(it) }
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

@Entity
@Serializable
class Survey24 : PanacheEntityBase() {
    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.UlidGenerator")
    @GeneratedValue(generator = "ulid_generator")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @Convert(converter = UlidConverter::class)
    val id: ULID? = null

    val title: String = ""

    val description: String? = null

    @Serializable(with = LocalDateSerializer::class)
    val createdAt: LocalDate = LocalDate.now()
}

@Entity(name = "survey")
@Serializable
class Survey(

    val title: String,
    val description: String? = null,
//
//    @Serializable(with = LocalDateSerializer::class)
//    val createdAt: LocalDate = LocalDate.now()
) {
    constructor() : this("", null)

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.UlidGenerator")
    @GeneratedValue(generator = "ulid_generator")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @Convert(converter = UlidConverter::class)
    val id: ULID? = null
//
//    val title: String = ""
//
//    val description: String? = null

    val createdAt: Instant? = null
}
