package com.fancia.backend.shared.common.tag.core.dto

import com.fancia.backend.shared.common.tag.core.enums.TagType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class TagItemRequest(
    @field:NotBlank(message = "Tag name is required")
    @field:Size(max = 100, message = "Tag must be at most 100 characters")
    val name: String,
    @field:NotNull(message = "Tag type is required")
    val type: TagType,
)
