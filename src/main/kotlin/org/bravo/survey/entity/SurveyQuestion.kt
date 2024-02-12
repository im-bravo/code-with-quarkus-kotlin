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
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JavaType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ulid.ULID

@Entity
@Table(name = "survey_question")
@Serializable
class SurveyQuestion (
    val title: String,
    val description: String?,
    val type: String,
    val required: Boolean = false,
    val order: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id", referencedColumnName = "id",  updatable = false)
    val survey: Survey
) {

    @Id
    @JavaType(value = UlidJavaType::class)
    @JdbcTypeCode(SqlTypes.BINARY)
    @GeneratedValue(generator = "ulid_generator", strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "ulid_generator", strategy = "org.bravo.survey.entity.UlidIdentifierGenerator")
    @Column(name = "id", insertable = true, updatable = false)
    @Convert(converter = UlidConverter::class)
    @Serializable(with = UlidSerializer::class)
    val id: ULID? = null

    @Column(name = "survey_id", insertable = false, updatable = false)
    @Convert(converter = UlidConverter::class)
    val surveyId: ULID = ULID.nextULID()

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var options: MutableSet<SurveyQuestionOption> = mutableSetOf()

    constructor() : this(
        "",
        "",
        "",
        false,
        0,
        Survey()
    )
}