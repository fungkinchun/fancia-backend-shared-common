package com.fancia.backend.shared.common.moderation.core.dto

import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import com.fancia.backend.shared.common.moderation.core.enums.ReportReason
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateReportRequest(
    @field:NotNull(message = "Target type is required")
    val targetType: BlockedResourceType,
    @field:NotNull(message = "Target id is required")
    val targetId: UUID,
    @field:NotNull(message = "Reason is required")
    val reason: ReportReason,
    @field:Size(max = 2000, message = "Details must be at most 2000 characters")
    val details: String? = null,
    val alsoBlockUser: Boolean = false,
    val alsoHideResource: Boolean = false,
    val targetOwnerUserId: UUID? = null,
)
