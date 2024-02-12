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
import org.bravo.survey.entity.Survey
import org.bravo.survey.repository.SurveyRepository


@Serializable
data class SurveyListResponse(val surveys: List<Survey>)

@Path("survey")
class SurveyController {

    @Inject
    lateinit var surveyRepository: SurveyRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllSurvey(): Uni<SurveyListResponse> {
        return surveyRepository.listAll().map { survey ->
            SurveyListResponse(survey)
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    fun createSurvey(survey: Survey): Uni<Survey> {
        return surveyRepository.persist(survey)
    }
}