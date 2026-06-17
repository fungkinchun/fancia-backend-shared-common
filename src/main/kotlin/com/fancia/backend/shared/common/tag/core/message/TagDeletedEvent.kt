package com.fancia.backend.shared.common.tag.core.message

import com.fancia.backend.shared.common.tag.core.enums.TagType
import java.util.*

data class TagDeletedEvent(
    val id: UUID,
    val name: String,
    val type: TagType,
)
