package com.fancia.backend.shared.common.core.validator

import jakarta.validation.Constraint

@Constraint(validatedBy = [UniqueValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Unique(
    val message: String = "Field must be unique",
    val columnName: String,
    val tableName: String
)