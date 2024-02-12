package org.bravo.survey.controller.request

data class CreateSurveyRequest(
    val title: String,
    val description: String?,
    val questions: List<Question>
) {
    data class Question(
        val title: String,
        val description: String?,
        val type: String,
        val options: List<String>
    )
}
