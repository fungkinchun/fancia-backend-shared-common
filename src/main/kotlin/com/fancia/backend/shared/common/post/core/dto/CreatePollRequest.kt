package com.fancia.backend.shared.common.post.core.dto

import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreatePollRequest(
    @field:Size(min = 2, max = 10, message = "Poll must have between 2 and 10 options")
    val options: List<@Size(max = 255) String> = emptyList(),
    val allowMultiple: Boolean = false,
    val closesAt: LocalDateTime? = null,
)
