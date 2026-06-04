package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class InvalidPostException(
    title: String = "Invalid Post",
    message: String,
    errorCode: String = "INVALID_POST",
) : DomainException(title, message, errorCode)
