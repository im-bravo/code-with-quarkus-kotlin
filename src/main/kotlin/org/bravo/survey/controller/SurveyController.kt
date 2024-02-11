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
import org.bravo.survey.entity.Survey
import org.bravo.survey.repository.SurveyRepository


@Path("survey")
class SurveyController {

    @Inject
    lateinit var surveyRepository: SurveyRepository

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllSurvey(): Uni<MutableList<Survey>> {
        return surveyRepository.listAll()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    fun createSurvey(survey: Survey): Uni<Survey> {
        return surveyRepository.persist(survey)
    }
}