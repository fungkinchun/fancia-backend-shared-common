package com.fancia.backend.shared.common.core.exception

class InvalidAuthenticationTokenException(
    title: String = "Invalid Authentication Token",
    message: String = "Invalid authentication token provided.",
    errorCode: String = "INVALID_AUTHENTICATION_TOKEN"
) : DomainException(title, message, errorCode)