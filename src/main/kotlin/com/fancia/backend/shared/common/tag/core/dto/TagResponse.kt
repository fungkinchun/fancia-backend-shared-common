package com.fancia.backend.shared.common.tag.core.dto

import java.time.LocalDateTime
import java.util.*

data class TagResponse(
    val name: String,
    var createdBy: UUID? = null,
    var createdAt: LocalDateTime? = null
)