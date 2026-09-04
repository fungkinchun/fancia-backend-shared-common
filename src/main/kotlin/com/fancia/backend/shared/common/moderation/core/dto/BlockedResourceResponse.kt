package com.fancia.backend.shared.common.moderation.core.dto

import com.fancia.backend.shared.common.core.utils.Default
import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import java.time.LocalDateTime
import java.util.UUID

data class BlockedResourceResponse @Default constructor(
    val resourceType: BlockedResourceType,
    val resourceId: UUID,
    val createdAt: LocalDateTime? = null,
)
