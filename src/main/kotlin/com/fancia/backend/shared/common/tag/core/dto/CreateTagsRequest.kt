package com.fancia.backend.shared.common.tag.core.dto

import jakarta.validation.Valid

data class CreateTagsRequest(
    @field:Valid
    val tags: List<@Valid TagItemRequest> = emptyList(),
)
