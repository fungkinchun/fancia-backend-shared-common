package com.fancia.backend.shared.common.core.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchValidator : ConstraintValidator<PasswordMatch?, Any?> {
    private var passwordFieldName: String? = null
    private var passwordMatchFieldName: String? = null
    override fun initialize(constraintAnnotation: PasswordMatch?) {
        passwordFieldName = constraintAnnotation?.passwordField
        passwordMatchFieldName = constraintAnnotation?.passwordConfirmationField
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null || passwordFieldName == null || passwordMatchFieldName == null) {
            return false
        }

        return try {
            val clazz = value.javaClass
            val passwordField = clazz.getDeclaredField(passwordFieldName!!).apply { isAccessible = true }
            val passwordMatchField = clazz.getDeclaredField(passwordMatchFieldName!!).apply { isAccessible = true }
            val password = passwordField.get(value) as? String
            val passwordMatch = passwordMatchField.get(value) as? String

            password != null && password == passwordMatch
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}