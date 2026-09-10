package com.fancia.backend.shared.common.core.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate

class ValidAgeRangeValidator : ConstraintValidator<AgeRange, LocalDate?> {
    private var minAge: Int = 18
    private var maxAge: Int = 120

    override fun initialize(constraintAnnotation: AgeRange) {
        minAge = constraintAnnotation.min
        maxAge = constraintAnnotation.max
        require(minAge >= 0) { "AgeRange.min must be >= 0" }
        require(maxAge >= minAge) { "AgeRange.max must be >= min" }
    }

    override fun isValid(value: LocalDate?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }
        val today = LocalDate.now()
        if (value.isAfter(today)) {
            return false
        }
        val latestAllowedBirthDate = today.minusYears(minAge.toLong())
        if (value.isAfter(latestAllowedBirthDate)) {
            return false
        }
        val earliestAllowedBirthDate = today.minusYears(maxAge.toLong())
        if (value.isBefore(earliestAllowedBirthDate)) {
            return false
        }
        return true
    }
}
