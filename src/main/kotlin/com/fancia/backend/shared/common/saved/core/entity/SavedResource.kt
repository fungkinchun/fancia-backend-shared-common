package com.fancia.backend.shared.common.saved.core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@Embeddable
data class SavedResourceId(
    @Column(name = "user_id", nullable = false)
    val userId: UUID = UUID(0, 0),
    @Column(name = "resource_id", nullable = false)
    val resourceId: UUID = UUID(0, 0),
) : Serializable {
    override fun equals(other: Any?): Boolean =
        other is SavedResourceId &&
            other.userId == userId &&
            other.resourceId == resourceId

    override fun hashCode(): Int = Objects.hash(userId, resourceId)
}

@Entity
@Table(name = "saved_resources")
class SavedResource(
    @EmbeddedId
    var id: SavedResourceId,
) {
    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
}
