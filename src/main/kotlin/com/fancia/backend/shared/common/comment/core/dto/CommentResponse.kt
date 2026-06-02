package com.fancia.backend.shared.common.comment.core.dto

import java.time.LocalDateTime
import java.util.UUID

data class CommentResponse(
    val id: UUID,
    val targetId: UUID,
    val authorUserId: UUID,
    val parentId: UUID?,
    val body: String,
    val createdAt: LocalDateTime?,
    val likeCount: Long = 0,
    val likedByCurrentUser: Boolean = false,
)
