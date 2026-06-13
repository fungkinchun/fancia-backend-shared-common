package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PostContentRequiredException(
    title: String = "Post Content Required",
    message: String = "Post must have a body or at least one media item",
    errorCode: String = "POST_CONTENT_REQUIRED",
) : DomainException(title, message, errorCode)
