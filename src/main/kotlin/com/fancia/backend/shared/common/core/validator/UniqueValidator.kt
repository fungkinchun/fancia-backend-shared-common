package com.fancia.backend.shared.common.core.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component

@Component
class UniqueValidator(private val jdbcClient: JdbcClient) : ConstraintValidator<Unique?, String?> {
    private var tableName: String? = null
    private var columnName: String? = null
    override fun initialize(constraintAnnotation: Unique?) {
        tableName = constraintAnnotation?.tableName
        columnName = constraintAnnotation?.columnName
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null || tableName.isNullOrEmpty() || columnName.isNullOrEmpty()) {
            return false
        }
        val sql = "SELECT COUNT(*) FROM $tableName WHERE $columnName = ?"
        val count = jdbcClient.sql(sql)
            .param(value)
            .query(Long::class.java)

        return count.single() == 0L
    }
}