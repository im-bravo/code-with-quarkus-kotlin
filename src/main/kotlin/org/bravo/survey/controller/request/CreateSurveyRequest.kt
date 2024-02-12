package org.bravo.survey.controller.request

import kotlinx.serialization.Serializable


@Serializable
data class CreateSurveyRequest(
    val title: String,
    val description: String?,
    val questions: Set<Question>
) {
    @Serializable
    data class Question(
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
}
