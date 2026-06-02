package com.fancia.backend.shared.common.comment.core.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateCommentRequest(
    @field:NotNull
    val targetId: UUID,
    @field:NotNull
    val authorUserId: UUID,
    @field:NotBlank(message = "Comment body is required")
    @field:Size(max = 4000, message = "Comment must be at most 4000 characters")
    val body: String,
    val parentId: UUID? = null,
)
