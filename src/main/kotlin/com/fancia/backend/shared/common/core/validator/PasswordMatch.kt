package com.fancia.backend.shared.common.core.validator

import jakarta.validation.Constraint

@Constraint(validatedBy = [PasswordMatchValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class PasswordMatch(
    val message: String = "Passwords do not match",
    val passwordField: String,
    val passwordConfirmationField: String
)