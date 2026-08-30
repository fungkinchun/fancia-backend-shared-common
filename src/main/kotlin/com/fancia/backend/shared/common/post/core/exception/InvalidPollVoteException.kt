package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class InvalidPollVoteException(
    title: String = "Invalid Poll Vote",
    message: String = "Vote options are invalid for this poll",
    errorCode: String = "INVALID_POLL_VOTE",
) : DomainException(title, message, errorCode)
