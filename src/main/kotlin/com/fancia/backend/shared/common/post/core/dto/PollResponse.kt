package com.fancia.backend.shared.common.post.core.dto

import java.time.LocalDateTime

data class PollResponse(
    val allowMultiple: Boolean,
    val closesAt: LocalDateTime?,
    val closed: Boolean,
    val totalVotes: Long,
    val options: List<PollOptionResponse>,
)
