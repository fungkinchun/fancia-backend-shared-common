package com.fancia.backend.shared.common.post.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class FeaturedPostRequiresMediaException(
    title: String = "Featured Post Requires Media",
    message: String = "Featured post must include at least one media item",
    errorCode: String = "FEATURED_POST_REQUIRES_MEDIA",
) : DomainException(title, message, errorCode)
