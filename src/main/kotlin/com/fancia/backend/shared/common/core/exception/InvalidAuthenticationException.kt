package com.fancia.backend.shared.common.core.exception

class InvalidAuthenticationException(
    title: String = "Invalid Authentication",
    message: String = "Invalid authentication credentials provided.",
    errorCode: String = "INVALID_AUTHENTICATION"
) : DomainException(title, message, errorCode)