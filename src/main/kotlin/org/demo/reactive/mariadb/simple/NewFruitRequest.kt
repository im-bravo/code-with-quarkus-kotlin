package org.demo.reactive.mariadb.simple

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class NewFruitRequest(
    @JsonProperty("name")
    @field:NotBlank(message = "Fruit name must not be blank")
//    @field:NotNull(message = "Fruit name must not be null")
    @get:JsonProperty("name")
    val name: String? = null
)
