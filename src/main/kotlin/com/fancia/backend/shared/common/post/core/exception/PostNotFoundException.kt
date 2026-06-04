package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException
import java.util.*

class PostNotFoundException(
    val postId: UUID,
    title: String = "Post Not Found",
    message: String = "Post not found with id: $postId",
    errorCode: String = "POST_NOT_FOUND",
) : DomainException(title, message, errorCode)
