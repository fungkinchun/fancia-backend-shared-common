package com.fancia.backend.shared.common.comment.core.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@Embeddable
data class CommentLikeId(
    @Column(name = "comment_id")
    var commentId: UUID,
    @Column(name = "user_id")
    var userId: UUID,
) : Serializable {
    override fun equals(other: Any?): Boolean =
        other is CommentLikeId &&
            other.commentId == commentId &&
            other.userId == userId

    override fun hashCode(): Int = Objects.hash(commentId, userId)
}

@Entity
@Table(name = "comment_likes")
class CommentLike(
    @EmbeddedId
    var id: CommentLikeId,
) {
    @MapsId("commentId")
    @ManyToOne
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    var comment: Comment? = null

    @CreationTimestamp
    var likedAt: LocalDateTime? = null
}
