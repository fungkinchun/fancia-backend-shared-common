package com.fancia.backend.shared.common.core.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateTagsRequest(
    @field:Valid
    val tags: List<@Valid SingleTagCreation> = emptyList()
)

data class SingleTagCreation(
    @field:NotBlank(message = "Tag name is required")
    @field:Size(
        min = 2,
        max = 50,
        message = "Tag name must be between 2 and 50 characters"
    )
    val name: String,
)