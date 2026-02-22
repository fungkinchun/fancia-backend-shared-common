package com.fancia.backend.shared.common.core.dto

import java.time.LocalDateTime

data class TagsResponse(
    val tags: List<TagResponse> = emptyList()
)

data class TagResponse(
    val name: String,
    var createdBy: Long = 0L,
    var createdAt: LocalDateTime? = null
)