package com.fancia.backend.shared.common.moderation.core.entity

import com.fancia.backend.shared.common.core.entity.AbstractEntity
import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import com.fancia.backend.shared.common.moderation.core.enums.ReportReason
import com.fancia.backend.shared.common.moderation.core.enums.ReportStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "reports")
class Report : AbstractEntity() {
    @Column(name = "reporter_user_id", nullable = false)
    var reporterUserId: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 32)
    var targetType: BlockedResourceType = BlockedResourceType.USER

    @Column(name = "target_id", nullable = false)
    var targetId: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    var reason: ReportReason = ReportReason.OTHER

    @Column(length = 2000)
    var details: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    var status: ReportStatus = ReportStatus.OPEN
}
