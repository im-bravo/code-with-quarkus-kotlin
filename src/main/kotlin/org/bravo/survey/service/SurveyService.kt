package org.bravo.survey.service

import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.bravo.survey.controller.response.SurveyDetail
import org.bravo.survey.entity.Survey
import org.bravo.survey.entity.SurveyQuestion
import org.bravo.survey.entity.SurveyQuestionOption
import org.bravo.survey.entity.UlidIdentifier
import org.bravo.survey.repository.SurveyRepository
import org.bravo.survey.service.input.CreateSurveyInput
import org.bravo.survey.service.input.CreateSurveyOutput
import ulid.ULID

@ApplicationScoped
class SurveyService {
    @Inject
    private lateinit var surveyRepository: SurveyRepository

    fun createSurvey( input: CreateSurveyInput): Uni<CreateSurveyOutput> {
        val surveyId = ULID.nextULID()
        val finalQuestions = mutableSetOf<SurveyQuestion>()
        val survey = Survey(
            id = UlidIdentifier(surveyId),
            title = input.title,
            description = input.description,
            questions = finalQuestions
        )
        input.questions.forEach { question ->
            val finalQuestionOption = mutableSetOf<SurveyQuestionOption>()
            val surveyQuestion = SurveyQuestion(
                id = UlidIdentifier(ULID.nextULID()),
                title = question.title,
                description = question.description,
                type = question.type,
                required = question.required,
                sort = question.sort,
                survey = survey,
            )

            question.options.forEach { option ->
                finalQuestionOption.add(
                    SurveyQuestionOption(
                        text = option.text,
                        value = option.value,
                        order = option.sort,
                        question = surveyQuestion
                    )
                )
            }
            surveyQuestion.options = finalQuestionOption
            finalQuestions.add(surveyQuestion)
        }
        return surveyRepository.persist(survey).map {
            CreateSurveyOutput(id = it.id.id)
        }
    }

    fun allSurvey(): Uni<List<SurveyDetail>> {
        return surveyRepository.listAll(Sort.by("id")).map { surveys ->
            surveys.map(SurveyDetail.Companion::fromEntity)
        }
    }

    fun oneSurvey(id: ULID): Uni<Survey> {
        return surveyRepository.findById(
            UlidIdentifier(id)
        )
    }
}