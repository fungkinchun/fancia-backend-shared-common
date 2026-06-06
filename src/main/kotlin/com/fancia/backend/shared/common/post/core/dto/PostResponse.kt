package com.fancia.backend.shared.common.post.core.dto

import java.time.LocalDateTime
import java.util.*

data class PostResponse(
    val id: UUID,
    val targetId: UUID,
    val authorUserId: UUID,
    val body: String?,
    val media: List<PostMediaResponse>,
    val featured: Boolean,
    val pinned: Boolean,
    val likeCount: Long = 0,
    val likedByCurrentUser: Boolean = false,
    val createdAt: LocalDateTime?,
)
