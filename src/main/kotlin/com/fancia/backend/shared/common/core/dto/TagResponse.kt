package com.fancia.backend.shared.common.core.dto

import java.time.LocalDateTime
import java.util.*

data class TagResponse(
    val name: String,
    var createdBy: UUID? = null,
    var createdAt: LocalDateTime? = null
)