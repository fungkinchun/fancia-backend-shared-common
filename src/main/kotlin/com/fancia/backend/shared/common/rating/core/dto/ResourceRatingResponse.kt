package com.fancia.backend.shared.common.rating.core.dto

import com.fancia.backend.shared.common.rating.core.enums.RatedResourceType
import java.time.LocalDateTime
import java.util.UUID

data class ResourceRatingResponse(
    val resourceType: RatedResourceType,
    val resourceId: UUID,
    val userId: UUID,
    val stars: Int,
    val updatedAt: LocalDateTime? = null,
)
