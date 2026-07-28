package com.fancia.backend.shared.common.tag.core.dto

import com.fancia.backend.shared.common.tag.core.enums.TagType
import java.time.LocalDateTime
import java.util.*

data class TagResponse(
    val id: UUID? = null,
    val name: String,
    val type: TagType,
    var createdBy: UUID? = null,
    var createdAt: LocalDateTime? = null,
)
