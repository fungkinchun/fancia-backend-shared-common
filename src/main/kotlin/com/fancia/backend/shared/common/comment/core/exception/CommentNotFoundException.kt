package com.fancia.backend.shared.common.comment.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException
import java.util.*

class CommentNotFoundException(
    val commentId: UUID,
    title: String = "Comment Not Found",
    message: String = "Comment not found with id: $commentId",
    errorCode: String = "COMMENT_NOT_FOUND",
) : DomainException(title, message, errorCode)
