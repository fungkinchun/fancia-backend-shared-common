package com.fancia.backend.shared.common.social.core.dto

import com.fancia.backend.shared.common.social.core.enums.LinkType

data class LinkResponse(
    val type: LinkType,
    val url: String,
)
