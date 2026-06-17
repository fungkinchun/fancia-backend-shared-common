package com.fancia.backend.shared.common.core.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate

class ValidAgeRangeValidator : ConstraintValidator<AgeRange, LocalDate?> {
    private var minAge: Int = 0
    private var maxAge: Int = 120

    override fun initialize(constraintAnnotation: AgeRange) {
        minAge = constraintAnnotation.min
        maxAge = constraintAnnotation.max
    }

    override fun isValid(value: LocalDate?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }
        val today = LocalDate.now()
        if (value.isAfter(today)) {
            return false
        }
        if (value.isAfter(today.minusYears(minAge.toLong()))) {
            return false
        }
        if (value.isBefore(today.minusYears(maxAge.toLong()))) {
            return false
        }
        return true
    }
}
