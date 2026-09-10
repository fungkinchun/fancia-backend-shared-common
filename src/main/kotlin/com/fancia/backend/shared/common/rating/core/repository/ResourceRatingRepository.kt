package com.fancia.backend.shared.common.rating.core.repository

import com.fancia.backend.shared.common.rating.core.entity.ResourceRating
import com.fancia.backend.shared.common.rating.core.entity.ResourceRatingId
import com.fancia.backend.shared.common.rating.core.enums.RatedResourceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ResourceRatingRepository : JpaRepository<ResourceRating, ResourceRatingId> {
    fun findByIdResourceTypeAndIdResourceIdAndIdUserId(
        resourceType: RatedResourceType,
        resourceId: UUID,
        userId: UUID,
    ): Optional<ResourceRating>

    fun deleteByIdResourceTypeAndIdResourceIdAndIdUserId(
        resourceType: RatedResourceType,
        resourceId: UUID,
        userId: UUID,
    )

    @Query(
        """
        SELECT AVG(r.stars)
        FROM ResourceRating r
        WHERE r.id.resourceType = :resourceType AND r.id.resourceId = :resourceId
        """,
    )
    fun averageStars(
        @Param("resourceType") resourceType: RatedResourceType,
        @Param("resourceId") resourceId: UUID,
    ): Double?

    fun countByIdResourceTypeAndIdResourceId(
        resourceType: RatedResourceType,
        resourceId: UUID,
    ): Long
}
