package org.bravo.survey.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import kotlinx.serialization.Serializable
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ulid.ULID


@Entity(name = "survey")
@Table(name = "survey")
@Serializable
class Survey (
    @EmbeddedId
    val id: UlidIdentifier,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String? = null,

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    val questions: MutableSet<SurveyQuestion> = mutableSetOf()
) {
    constructor() : this(UlidIdentifier(ULID.nextULID()),"", null)

//    @Id
//    @JavaType(value = UlidJavaType::class)
//    @JdbcTypeCode(SqlTypes.BINARY)
//    @GeneratedValue(generator = "ulid_generator", strategy = GenerationType.IDENTITY)
//    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.UlidIdentifierGenerator")
//    @Column(name = "id", insertable = true, updatable = false)
//    @Convert(converter = UlidConverter::class)
//    @Serializable(with = UlidSerializer::class)
//    val id: ULID? = null

    @Serializable(with = InstantSerializer::class)
    @Column(name = "created_at", insertable = true, updatable = false)
    val createdAt: Instant = Instant.now()

}
