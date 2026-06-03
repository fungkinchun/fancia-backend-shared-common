package com.fancia.backend.shared.common.comment.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException
import java.util.UUID

class CommentAccessDeniedException(
    val targetId: UUID,
    title: String = "Comment Not Allowed",
    message: String = "You are not allowed to comment on resource: $targetId",
    errorCode: String = "COMMENT_ACCESS_DENIED",
) : DomainException(title, message, errorCode)
