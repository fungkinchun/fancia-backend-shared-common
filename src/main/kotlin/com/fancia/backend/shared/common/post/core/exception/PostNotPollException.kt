package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PostNotPollException(
    title: String = "Not a Poll",
    message: String = "This post is not a poll",
    errorCode: String = "POST_NOT_POLL",
) : DomainException(title, message, errorCode)
