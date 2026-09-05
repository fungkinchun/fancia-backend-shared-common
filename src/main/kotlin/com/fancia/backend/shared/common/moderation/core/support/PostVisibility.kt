package com.fancia.backend.shared.common.moderation.core.support

import com.fancia.backend.shared.common.post.core.dto.PostResponse
import java.util.UUID

object PostVisibility {
    fun isVisibleToViewer(
        post: PostResponse,
        blockedPostIds: Set<UUID>,
        blockedUserIds: Set<UUID>,
    ): Boolean {
        if (post.id in blockedPostIds) return false
        if (post.authorUserId in blockedUserIds) return false
        return true
    }
}
