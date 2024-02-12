package org.bravo.survey.service.input

data class CreateSurveyInput(
    val title: String,
    val description: String?,
    val questions: MutableSet<Question>
) {
    data class Question(
        val title: String,
        val description: String?,
        val type: String,
        val order: Int,
        val required: Boolean,
        val options: MutableSet<Option>
    ) {
        data class Option (
            val value: Int,
            val text: String,
            val order: Int,
        )
    }
}
