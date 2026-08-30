package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PostExpiredException(
    title: String = "Post Expired",
    message: String = "Expired posts are immutable except for status changes",
    errorCode: String = "POST_EXPIRED",
) : DomainException(title, message, errorCode)
