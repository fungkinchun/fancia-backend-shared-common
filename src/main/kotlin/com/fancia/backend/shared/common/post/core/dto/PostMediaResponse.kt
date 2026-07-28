package com.fancia.backend.shared.common.post.core.dto

import com.fancia.backend.shared.common.post.core.enums.PostMediaType

data class PostMediaResponse(
    val objectKey: String,
    val mediaType: PostMediaType,
    val sortOrder: Int,
)
