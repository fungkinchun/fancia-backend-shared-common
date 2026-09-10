package com.fancia.backend.shared.common.rating.core.dto

import com.fancia.backend.shared.common.rating.core.enums.RatedResourceType
import java.util.UUID

data class ResourceRatingSummaryResponse(
    val resourceType: RatedResourceType,
    val resourceId: UUID,
    val averageRating: Double = 0.0,
    val ratingCount: Long = 0,
    val currentUserRating: Int? = null,
)
