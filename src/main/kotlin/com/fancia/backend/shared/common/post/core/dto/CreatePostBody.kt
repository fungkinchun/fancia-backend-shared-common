package com.fancia.backend.shared.common.post.core.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Size

data class CreatePostBody(
    @field:Size(max = 4000, message = "Post body must be at most 4000 characters")
    val body: String? = null,
    @field:Valid
    val media: List<PostMediaItem> = emptyList(),
    @JsonProperty("isFeatured")
    val isFeatured: Boolean = false,
    @JsonProperty("isPinned")
    val isPinned: Boolean = false,
)
