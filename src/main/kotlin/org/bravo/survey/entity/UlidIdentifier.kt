package org.bravo.survey.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import kotlinx.serialization.Serializable
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ulid.ULID

@Embeddable
@Serializable
data class UlidIdentifier (
    @JavaType(value = UlidJavaType::class)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Convert(converter = UlidConverter::class)
    @Column(name = "id", insertable = true, updatable = false)
    @Serializable(with = UlidSerializer::class)
    val id: ULID = ULID.nextULID()
) : java.io.Serializable {

    companion object {
        const val serialVersionUID = -4395439869491L
    }
}