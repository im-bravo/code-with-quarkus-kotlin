package org.bravo.survey.controller.response

import kotlinx.serialization.Serializable
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
}
