package com.fancia.backend.shared.common.post.core.dto

import com.fancia.backend.shared.common.post.core.enums.PostKind
import com.fancia.backend.shared.common.post.core.enums.PostStatus
import java.time.LocalDateTime
import java.util.*

data class PostResponse(
    val id: UUID,
    val targetId: UUID,
    val authorUserId: UUID,
    val body: String?,
    val media: List<PostMediaResponse>,
    val status: PostStatus = PostStatus.VISIBLE,
    val expiredAt: LocalDateTime? = null,
    val likeCount: Long = 0,
    val likedByCurrentUser: Boolean = false,
    val createdAt: LocalDateTime?,
    val kind: PostKind = PostKind.TEXT,
    val poll: PollResponse? = null,
)
