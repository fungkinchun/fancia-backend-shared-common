package com.fancia.backend.shared.common.core.dto

import com.fancia.backend.shared.common.core.enums.LinkType

data class LinkResponse(
    val type: LinkType,
    val url: String,
)
