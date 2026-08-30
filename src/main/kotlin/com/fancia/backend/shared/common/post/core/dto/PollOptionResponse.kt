package com.fancia.backend.shared.common.post.core.dto

import java.util.*

data class PollOptionResponse(
    val id: UUID,
    val label: String,
    val sortOrder: Int,
    val voteCount: Long = 0,
    val selectedByCurrentUser: Boolean = false,
)
