package org.bravo.survey.controller.request

import kotlinx.serialization.Serializable
import org.bravo.survey.service.input.CreateSurveyInput


@Serializable
data class CreateSurveyRequest(
    val title: String,
    val description: String?,
    val questions: List<Question>
) {
    @Serializable
    data class Question(
        val title: String,
        val description: String?,
        val type: String,
        val options: List<Option>
    ) {
        @Serializable
        data class Option(
            val text: String,
            val value: Int,
            val order: Int
        )
    }
}
