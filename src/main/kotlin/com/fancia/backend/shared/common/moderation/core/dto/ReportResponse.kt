package com.fancia.backend.shared.common.moderation.core.dto

import com.fancia.backend.shared.common.core.utils.Default
import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import com.fancia.backend.shared.common.moderation.core.enums.ReportReason
import com.fancia.backend.shared.common.moderation.core.enums.ReportStatus
import java.time.LocalDateTime
import java.util.UUID

data class ReportResponse @Default constructor(
    val id: UUID? = null,
    val targetType: BlockedResourceType,
    val targetId: UUID,
    val reason: ReportReason,
    val details: String? = null,
    val status: ReportStatus = ReportStatus.OPEN,
    val createdAt: LocalDateTime? = null,
)
