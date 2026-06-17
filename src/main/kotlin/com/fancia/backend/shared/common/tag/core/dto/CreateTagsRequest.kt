package com.fancia.backend.shared.common.tag.core.dto

import com.fancia.backend.shared.common.tag.core.enums.TagType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateTagsRequest(
    @field:Valid
    val tags: List<@Valid SingleTagCreation> = emptyList(),
)

data class SingleTagCreation(
    @field:NotBlank(message = "Tag name is required")
    @field:Size(
        min = 2,
        max = 50,
        message = "Tag name must be between 2 and 50 characters",
    )
    val name: String,
    @field:NotNull(message = "Tag type is required")
    val type: TagType = TagType.INTEREST,
)
