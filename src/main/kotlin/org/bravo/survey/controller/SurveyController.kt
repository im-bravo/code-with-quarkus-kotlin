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
import org.bravo.survey.controller.request.CreateSurveyRequest
import org.bravo.survey.controller.response.SurveyListResponse
import org.bravo.survey.service.SurveyService
import org.bravo.survey.service.input.CreateSurveyInput
import org.bravo.survey.service.input.CreateSurveyOutput


@Path("survey")
class SurveyController {

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
                sort = question.sort,
                required = question.required,
                options = mutableSetOf()
            )
            question.options.forEach { option ->
                surveyQuestion.options.add(
                    CreateSurveyInput.Question.Option(
                        text = option.text,
                        value = option.value,
                        sort = option.sort,
                    )
                )
            }
            survey.questions.add(surveyQuestion)
        }
        return service.createSurvey(survey)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    fun getAllSurvey(): Uni<SurveyListResponse> {
        return service.allSurvey().map { survey ->
            SurveyListResponse(survey)
        }
    }
}