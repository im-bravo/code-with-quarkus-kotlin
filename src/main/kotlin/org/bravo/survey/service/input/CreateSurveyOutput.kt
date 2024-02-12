package org.bravo.survey.service.input

import kotlinx.serialization.Serializable
import org.bravo.survey.entity.UlidSerializer
import ulid.ULID

@Serializable
data class CreateSurveyOutput(
    @Serializable(with = UlidSerializer::class)
    val id: ULID
)