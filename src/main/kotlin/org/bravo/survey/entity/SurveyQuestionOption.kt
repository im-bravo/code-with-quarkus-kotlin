package org.bravo.survey.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import kotlinx.serialization.Serializable
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ulid.ULID

@Entity
@Table(name = "survey_question_option")
@Serializable
class SurveyQuestionOption (
    @Column(name = "text")
    val text: String,

    @Column(name = "value")
    val value: Int,

    @Column(name = "sort")
    val order: Int,

    @Serializable(with = InstantSerializer::class)
    @Column(name = "created_at", insertable = true, updatable = false)
    val createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", referencedColumnName = "id",  updatable = false)
    var question: SurveyQuestion
) {
    constructor() : this(
        "",
        0,
        0,
        Instant.now(),
        SurveyQuestion()
    )


    @Id
    @JavaType(value = UlidJavaType::class)
    @JdbcTypeCode(SqlTypes.BINARY)
    @GeneratedValue(generator = "ulid_generator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.UlidIdentifierGenerator")
    @Column(name = "id", insertable = true, updatable = false)
    @Convert(converter = UlidConverter::class)
    @Serializable(with = UlidSerializer::class)
    val id: ULID? = null


    @Column(name = "question_id", insertable = false, updatable = false)
    @Convert(converter = UlidConverter::class)
    @Serializable(with = UlidSerializer::class)
    val questionId: ULID? = null

}