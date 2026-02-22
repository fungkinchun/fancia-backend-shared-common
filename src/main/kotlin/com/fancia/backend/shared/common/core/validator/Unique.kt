package com.fancia.backend.shared.common.core.validator

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [UniqueValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Unique(
    val message: String = "Field must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = [],
    val columnName: String,
    val tableName: String
)