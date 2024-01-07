package org.demo.reactive.mariadb.simple

import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid

@ApplicationScoped
class FruitService {
    fun validateFruit(@Valid fruit: NewFruitRequest) {

    }
}