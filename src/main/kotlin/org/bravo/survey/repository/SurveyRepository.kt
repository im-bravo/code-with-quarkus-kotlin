package org.bravo.survey.repository

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import org.bravo.survey.entity.Survey
import ulid.ULID


@ApplicationScoped
class SurveyRepository : PanacheRepositoryBase<Survey, ULID> {

}