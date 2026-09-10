package com.fancia.backend.shared.common.rating.core.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class UpsertResourceRatingRequest(
    @field:Min(1)
    @field:Max(5)
    val stars: Int,
)
