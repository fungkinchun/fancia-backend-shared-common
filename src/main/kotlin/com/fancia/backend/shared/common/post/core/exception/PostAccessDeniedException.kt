package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException
import java.util.*

class PostAccessDeniedException(
    val targetId: UUID,
    title: String = "Post Not Allowed",
    message: String = "You are not allowed to post on resource: $targetId",
    errorCode: String = "POST_ACCESS_DENIED",
) : DomainException(title, message, errorCode)
