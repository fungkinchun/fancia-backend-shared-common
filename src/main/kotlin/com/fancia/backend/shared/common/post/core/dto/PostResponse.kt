package com.fancia.backend.shared.common.post.core.dto

import java.time.LocalDateTime
import java.util.*

data class PostResponse(
    val id: UUID,
    val targetId: UUID,
    val authorUserId: UUID,
    val body: String?,
    val media: List<PostMediaResponse>,
    val createdAt: LocalDateTime?,
)
