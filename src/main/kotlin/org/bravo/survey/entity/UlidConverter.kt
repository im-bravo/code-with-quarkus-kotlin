package org.bravo.survey.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import ulid.ULID

@Converter
class UlidConverter : AttributeConverter<ULID, ByteArray> {
    override fun convertToDatabaseColumn(attribute: ULID?): ByteArray? = attribute?.toBytes()
    override fun convertToEntityAttribute(dbData: ByteArray?): ULID? = dbData?.let(ULID::fromBytes)
}