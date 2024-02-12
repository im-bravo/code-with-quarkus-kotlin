package org.bravo.survey.validation

import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass
import ulid.ULID

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target( AnnotationTarget.VALUE_PARAMETER , AnnotationTarget.FIELD)
@Constraint(validatedBy = [UlidParameterValidator::class])
annotation class ValidUlid(
    val message: String = "Invalid ULID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

@ApplicationScoped
class UlidParameterValidator : ConstraintValidator<ValidUlid, String> {
    private lateinit var annotation: ValidUlid

    override fun initialize(annotation: ValidUlid) {
        // No initialization needed here
        this.annotation = annotation
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        try {
            ULID.parseULID(value)
            return true
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}


