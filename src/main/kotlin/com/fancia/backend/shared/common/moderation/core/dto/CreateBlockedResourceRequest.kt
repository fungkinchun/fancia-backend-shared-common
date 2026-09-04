package com.fancia.backend.shared.common.moderation.core.dto

import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateBlockedResourceRequest(
    @field:NotNull(message = "Resource type is required")
    val resourceType: BlockedResourceType,
    @field:NotNull(message = "Resource id is required")
    val resourceId: UUID,
)
