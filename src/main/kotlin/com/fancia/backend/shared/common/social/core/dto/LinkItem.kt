package com.fancia.backend.shared.common.social.core.dto

import com.fancia.backend.shared.common.social.core.enums.LinkType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LinkItem(
    @field:NotNull
    val type: LinkType,

    @field:NotBlank
    @field:Size(max = 255, message = "Link URL must be at most 255 characters")
    val url: String,
)
