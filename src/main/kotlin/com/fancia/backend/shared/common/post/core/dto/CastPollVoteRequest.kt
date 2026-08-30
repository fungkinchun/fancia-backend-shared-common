package com.fancia.backend.shared.common.post.core.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.util.*

data class CastPollVoteRequest(
    @field:NotEmpty(message = "Select at least one option")
    @field:Size(max = 10, message = "Too many options selected")
    val optionIds: List<UUID> = emptyList(),
)
