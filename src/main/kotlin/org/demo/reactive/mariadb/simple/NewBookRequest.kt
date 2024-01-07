package org.demo.reactive.mariadb.simple

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class NewBookRequest(
    @JsonProperty("name")
    @field:NotBlank(message = "Fruit name must not be blank")
    @get:JsonProperty("name")
    val name: String? = null
)
