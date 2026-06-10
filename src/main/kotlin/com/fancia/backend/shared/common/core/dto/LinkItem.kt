package com.fancia.backend.shared.common.core.dto

import com.fancia.backend.shared.common.core.enums.LinkType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LinkItem(
    @field:NotNull
    val type: LinkType,

    @field:NotBlank
    @field:Size(max = 2048)
    val url: String,
)
