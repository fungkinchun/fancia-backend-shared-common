package com.fancia.backend.shared.common.core.exception

class PremiumFeatureLimitException(
    message: String,
    errorCode: String = "PREMIUM_FEATURE_LIMIT",
    title: String = "Plan Limit",
) : DomainException(title = title, message = message, errorCode = errorCode)
