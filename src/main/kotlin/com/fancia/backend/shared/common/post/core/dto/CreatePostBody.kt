package com.fancia.backend.shared.common.post.core.dto

import com.fancia.backend.shared.common.post.core.enums.PostKind
import com.fancia.backend.shared.common.post.core.enums.PostStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreatePostBody(
    @field:Size(max = 4000, message = "Post body must be at most 4000 characters")
    val body: String? = null,
    @field:Valid
    val media: List<PostMediaItem>? = null,
    val status: PostStatus? = null,
    val expiredAt: LocalDateTime? = null,
    val kind: PostKind? = null,
    @field:Valid
    val poll: CreatePollRequest? = null,
) {
    fun mediaOrEmpty(): List<PostMediaItem> = media.orEmpty()

    fun statusOrDefault(): PostStatus = status ?: PostStatus.VISIBLE

    fun kindOrDefault(): PostKind = kind ?: PostKind.TEXT
}
