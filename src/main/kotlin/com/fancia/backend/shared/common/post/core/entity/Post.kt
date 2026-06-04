package com.fancia.backend.shared.common.post.core.entity

import com.fancia.backend.shared.common.core.entity.AbstractEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "posts")
class Post(
    @Column(name = "target_id", nullable = false)
    var targetId: UUID,
    @Column(name = "author_user_id", nullable = false)
    var authorUserId: UUID,
    @Column(length = 4000)
    var body: String? = null,
) : AbstractEntity() {
    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    val media: MutableList<PostMedia> = mutableListOf()
}
