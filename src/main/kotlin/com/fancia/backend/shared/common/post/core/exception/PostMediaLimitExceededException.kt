package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PostMediaLimitExceededException(
    val maxMedia: Int,
    title: String = "Too Many Media Items",
    message: String = "Post may have at most $maxMedia media items",
    errorCode: String = "POST_MEDIA_LIMIT_EXCEEDED",
) : DomainException(title, message, errorCode)
