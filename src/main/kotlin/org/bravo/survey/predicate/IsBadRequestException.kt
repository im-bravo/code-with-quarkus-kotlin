package org.bravo.survey.predicate

import jakarta.json.JsonException
import java.util.function.Predicate

object IsBadRequestException : Predicate<Throwable> {
    override fun test(t: Throwable): Boolean {
        return t is JsonException
    }
}