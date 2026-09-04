package com.fancia.backend.shared.common.saved.core.dto

import java.time.LocalDateTime
import java.util.UUID

data class SavedResourceResponse(
    val resourceId: UUID,
    val createdAt: LocalDateTime? = null,
)
