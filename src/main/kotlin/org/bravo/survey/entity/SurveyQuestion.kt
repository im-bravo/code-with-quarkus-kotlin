package org.bravo.survey.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ulid.ULID

@Entity
@Table(name = "survey_question")
@Serializable
class SurveyQuestion (
    @EmbeddedId
    val id: UlidIdentifier,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String?,

    @Column(name = "type")
    val type: String,

    @Column(name = "required")
    val required: Boolean = false,

    @Column(name = "sort")
    val sort: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id", updatable = false)
    val survey: Survey
) {

    @JavaType(value = UlidJavaType::class)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Convert(converter = UlidConverter::class)
    @Serializable(with = UlidSerializer::class)
    @Column(name = "survey_id", insertable = false, updatable = false)
    val surveyId: ULID = ULID.nextULID()

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    var options: MutableSet<SurveyQuestionOption> = mutableSetOf()

    constructor() : this(
        UlidIdentifier(ULID.nextULID()),
        "",
        "",
        "",
        false,
        0,
        Survey()
    )
}