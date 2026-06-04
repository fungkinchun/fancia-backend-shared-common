package com.fancia.backend.shared.common.comment.core.entity

import com.fancia.backend.shared.common.core.entity.AbstractEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "comments")
class Comment(
    @Column(name = "target_id", nullable = false)
    var targetId: UUID,
    @Column(name = "author_user_id", nullable = false)
    var authorUserId: UUID,
    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent: Comment? = null,
    @Column(name = "post_id")
    var postId: UUID? = null,
    @Column(nullable = false, length = 4000)
    var body: String,
) : AbstractEntity() {
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("createdAt ASC")
    val replies: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableSet<CommentLike> = mutableSetOf()
}
