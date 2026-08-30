package com.fancia.backend.shared.common.post.core.dto

import com.fancia.backend.shared.common.post.core.enums.PostKind
import com.fancia.backend.shared.common.post.core.enums.PostStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.*

data class CreatePostRequest(
    @field:NotNull
    val targetId: UUID? = null,
    @field:NotNull
    val authorUserId: UUID? = null,
    @field:Size(max = 4000, message = "Post body must be at most 4000 characters")
    val body: String? = null,
    @field:Valid
    val media: List<PostMediaItem> = emptyList(),
    val status: PostStatus = PostStatus.VISIBLE,
    val expiredAt: LocalDateTime? = null,
    val kind: PostKind = PostKind.TEXT,
    @field:Valid
    val poll: CreatePollRequest? = null,
)
