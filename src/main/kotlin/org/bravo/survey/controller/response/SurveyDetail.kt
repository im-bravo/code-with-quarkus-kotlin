package org.bravo.survey.controller.response

import kotlinx.serialization.Serializable
import org.bravo.survey.entity.Survey
import ulid.ULID


@Serializable
data class SurveyDetail(
    val id: String,
    val title: String,
    val description: String?,
    val questions: Set<Question>
) {
    @Serializable
    data class Question(
        val id: String,
        val title: String,
        val description: String?,
        val type: String,
        val sort: Int,
        val required: Boolean,
        val options: Set<Option>
    ) {
        @Serializable
        data class Option(
            val text: String,
            val value: Int,
            val sort: Int
        )
    }

    companion object {
        fun fromEntity(
            entity: Survey
        ): SurveyDetail {
            return SurveyDetail(
                id = entity.id.id.toString(),
                title = entity.title,
                description = entity.description,
                questions = entity.questions.map { question ->
                    Question(
                        id = question.id.id.toString(),
                        title = question.title,
                        description = question.description,
                        type = question.type,
                        sort = question.sort,
                        required = question.required,
                        options = question.options.map { option ->
                            Question.Option(
                                text = option.text,
                                value = option.value,
                                sort = option.order
                            )
                        }.toSet()
                    )
                }.toSet()
            )
        }
    }
}
