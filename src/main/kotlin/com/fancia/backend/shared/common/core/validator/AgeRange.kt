package com.fancia.backend.shared.common.core.validator

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [ValidAgeRangeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AgeRange(
    val message: String = "You must be at least {min} years old",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = [],
    val min: Int = 18,
    val max: Int = 120,
)
