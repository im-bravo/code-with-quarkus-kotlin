package org.bravo.survey.service

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bravo.survey.entity.Survey
import org.bravo.survey.entity.SurveyQuestion
import org.bravo.survey.entity.SurveyQuestionOption
import org.bravo.survey.repository.SurveyRepository
import org.bravo.survey.service.input.CreateSurveyInput
import org.bravo.survey.service.input.CreateSurveyOutput

@ApplicationScoped
class SurveyService {
    @Inject
    private lateinit var surveyRepository: SurveyRepository

    fun createSurvey( input: CreateSurveyInput): Uni<CreateSurveyOutput> {
        val finalQuestions = mutableSetOf<SurveyQuestion>()
        val survey = Survey(
            title = input.title,
            description = input.description,
            questions = finalQuestions
        )
        input.questions.forEach { question ->
            val finalQuestionOption = mutableSetOf<SurveyQuestionOption>()
            val surveyQuestion = SurveyQuestion(
                title = question.title,
                description = question.description,
                type = question.type,
                required = question.required,
                order = question.order,
                survey = survey,
            )

            question.options.forEach { option ->
                finalQuestionOption.add(
                    SurveyQuestionOption(
                        text = option.text,
                        value = option.value,
                        order = option.order,
                        question = surveyQuestion
                    )
                )
            }
            surveyQuestion.options = finalQuestionOption
            finalQuestions.add(surveyQuestion)
        }
        return surveyRepository.persist(survey).map {
            it.id?.let { it1 -> CreateSurveyOutput(id = it1) }
        }
    }
}