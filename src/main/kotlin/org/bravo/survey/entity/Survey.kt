package org.bravo.survey.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import kotlinx.serialization.Serializable
import ulid.ULID


@Entity(name = "survey")
@Table(name = "survey")
@Serializable
class Survey (
    @EmbeddedId
    val id: UlidIdentifier,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String? = null,

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    val questions: MutableSet<SurveyQuestion> = mutableSetOf()
) {
    constructor() : this(UlidIdentifier(ULID.nextULID()),"", null)

    @Serializable(with = InstantSerializer::class)
    @Column(name = "created_at", insertable = true, updatable = false)
    val createdAt: Instant = Instant.now()

}
