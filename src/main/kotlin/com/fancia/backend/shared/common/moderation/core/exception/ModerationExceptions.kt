package com.fancia.backend.shared.common.moderation.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class UnsupportedBlockedResourceTypeException(
    title: String = "Unsupported Resource Type",
    message: String = "This service does not support blocking or reporting that resource type",
    errorCode: String = "UNSUPPORTED_BLOCKED_RESOURCE_TYPE",
) : DomainException(title, message, errorCode)

class SelfBlockException(
    title: String = "Cannot Block Self",
    message: String = "You cannot block yourself",
    errorCode: String = "SELF_BLOCK",
) : DomainException(title, message, errorCode)
