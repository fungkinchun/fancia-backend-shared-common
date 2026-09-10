package com.fancia.backend.shared.common.rating.core.support

import com.fancia.backend.shared.common.rating.core.enums.RatedResourceType
import com.fancia.backend.shared.common.rating.core.repository.ResourceRatingRepository
import java.util.UUID

data class ResourceRatingEnrichment(
    val averageRating: Double,
    val ratingCount: Long,
    val currentUserRating: Int?,
)

fun ResourceRatingRepository.loadEnrichment(
    resourceType: RatedResourceType,
    resourceId: UUID,
    userId: UUID?,
): ResourceRatingEnrichment {
    val count = countByIdResourceTypeAndIdResourceId(resourceType, resourceId)
    val average = if (count == 0L) 0.0 else (averageStars(resourceType, resourceId) ?: 0.0)
    val current = userId?.let {
        findByIdResourceTypeAndIdResourceIdAndIdUserId(resourceType, resourceId, it)
            .map { rating -> rating.stars }
            .orElse(null)
    }
    return ResourceRatingEnrichment(
        averageRating = average,
        ratingCount = count,
        currentUserRating = current,
    )
}
