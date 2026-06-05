package com.fancia.backend.shared.common.post.core.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Size

data class UpdatePostRequest(
    @field:Size(max = 4000, message = "Post body must be at most 4000 characters")
    val body: String? = null,
    @field:Valid
    val media: List<PostMediaItem> = emptyList(),
    val isFeatured: Boolean = false,
    val isPinned: Boolean = false,
)
