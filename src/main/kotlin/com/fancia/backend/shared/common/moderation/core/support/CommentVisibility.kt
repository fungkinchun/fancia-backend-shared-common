package com.fancia.backend.shared.common.moderation.core.support

import com.fancia.backend.shared.common.comment.core.dto.CommentResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.UUID

object CommentVisibility {
    fun isVisibleToViewer(
        comment: CommentResponse,
        blockedCommentIds: Set<UUID>,
        blockedUserIds: Set<UUID>,
    ): Boolean {
        if (comment.id in blockedCommentIds) return false
        if (comment.authorUserId in blockedUserIds) return false
        return true
    }

    fun filterPage(
        page: Page<CommentResponse>,
        pageable: Pageable,
        blockedCommentIds: Set<UUID>,
        blockedUserIds: Set<UUID>,
    ): Page<CommentResponse> {
        if (page.isEmpty || (blockedCommentIds.isEmpty() && blockedUserIds.isEmpty())) return page
        val kept = page.content.filter {
            isVisibleToViewer(it, blockedCommentIds, blockedUserIds)
        }
        if (kept.size == page.content.size) return page
        return PageImpl(kept, pageable, page.totalElements)
    }
}
