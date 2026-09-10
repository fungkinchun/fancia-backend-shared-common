package com.fancia.backend.shared.common.rating.core.entity

import com.fancia.backend.shared.common.rating.core.enums.RatedResourceType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@Embeddable
data class ResourceRatingId(
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 32)
    val resourceType: RatedResourceType = RatedResourceType.EVENT,
    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID = UUID(0, 0),
    @Column(name = "user_id", nullable = false)
    val userId: UUID = UUID(0, 0),
) : Serializable {
    override fun equals(other: Any?): Boolean =
        other is ResourceRatingId &&
            other.resourceType == resourceType &&
            other.resourceId == resourceId &&
            other.userId == userId

    override fun hashCode(): Int = Objects.hash(resourceType, resourceId, userId)
}

@Entity
@Table(name = "resource_ratings")
class ResourceRating(
    @EmbeddedId
    var id: ResourceRatingId,
    @Column(name = "stars", nullable = false)
    var stars: Int,
) {
    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
}
