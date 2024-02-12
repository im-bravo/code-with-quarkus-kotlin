package org.bravo.survey.controller.response

import kotlinx.serialization.Serializable
import org.bravo.survey.entity.Survey

@Serializable
data class Survey2ListResponse(val surveys: List<Survey>)