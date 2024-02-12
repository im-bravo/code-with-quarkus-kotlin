package org.bravo.survey.controller

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.serialization.Serializable
import org.bravo.survey.controller.request.CreateSurveyRequest
import org.bravo.survey.entity.Survey
import org.bravo.survey.entity.SurveyQuestion
import org.bravo.survey.entity.SurveyQuestionOption
import org.bravo.survey.repository.SurveyRepository
import org.bravo.survey.service.SurveyService
import org.bravo.survey.service.input.CreateSurveyInput
import org.bravo.survey.service.input.CreateSurveyOutput


@Serializable
data class Survey2ListResponse(val surveys: List<Survey>)

@Path("survey2")
class Survey2Controller {

    @Inject
    lateinit var service: SurveyService

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    fun createSurvey(input: CreateSurveyRequest): Uni<CreateSurveyOutput> {
        val survey = CreateSurveyInput(
            title = input.title,
            description = input.description,
            questions = mutableSetOf()
        )
        input.questions.forEach { question ->
            val surveyQuestion = CreateSurveyInput.Question(
                title = question.title,
                description = question.description,
                type = question.type,
                order = question.order,
                required = question.required,
                options = mutableSetOf()
            )
            question.options.forEach { option ->
                surveyQuestion.options.add(
                    CreateSurveyInput.Question.Option(
                        text = option.text,
                        value = option.value,
                        order = option.order,
                    )
                )
            }
            survey.questions.add(surveyQuestion)
        }
        return service.createSurvey(survey)
    }
}