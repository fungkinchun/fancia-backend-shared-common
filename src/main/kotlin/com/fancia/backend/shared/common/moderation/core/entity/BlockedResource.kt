package com.fancia.backend.shared.common.moderation.core.entity

import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@Embeddable
data class BlockedResourceId(
    @Column(name = "user_id", nullable = false)
    val userId: UUID = UUID(0, 0),
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 32)
    val resourceType: BlockedResourceType = BlockedResourceType.USER,
    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID = UUID(0, 0),
) : Serializable {
    override fun equals(other: Any?): Boolean =
        other is BlockedResourceId &&
            other.userId == userId &&
            other.resourceType == resourceType &&
            other.resourceId == resourceId

    override fun hashCode(): Int = Objects.hash(userId, resourceType, resourceId)
}

@Entity
@Table(name = "blocked_resources")
class BlockedResource(
    @EmbeddedId
    var id: BlockedResourceId,
) {
    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
}
