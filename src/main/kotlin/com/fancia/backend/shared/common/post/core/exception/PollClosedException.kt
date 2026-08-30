package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class PollClosedException(
    title: String = "Poll Closed",
    message: String = "This poll is closed and no longer accepts votes",
    errorCode: String = "POLL_CLOSED",
) : DomainException(title, message, errorCode)
