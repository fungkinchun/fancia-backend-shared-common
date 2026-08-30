package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PollRequiredException(
    title: String = "Poll Required",
    message: String = "Poll posts require a poll with between 2 and 10 options",
    errorCode: String = "POLL_REQUIRED",
) : DomainException(title, message, errorCode)
