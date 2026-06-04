package com.fancia.backend.shared.common.post.core.dto

import com.fancia.backend.shared.common.post.core.enums.PostMediaType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PostMediaItem(
    @field:NotBlank
    @field:Size(max = 1024)
    val objectKey: String,
    val mediaType: PostMediaType = PostMediaType.IMAGE,
)
