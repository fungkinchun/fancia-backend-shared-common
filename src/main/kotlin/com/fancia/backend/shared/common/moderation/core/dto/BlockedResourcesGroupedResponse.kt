package com.fancia.backend.shared.common.moderation.core.dto

import com.fancia.backend.shared.common.core.utils.Default
import com.fancia.backend.shared.common.moderation.core.enums.BlockedResourceType
import java.util.UUID

data class BlockedResourcesGroupedResponse @Default constructor(
    val blocked: Map<BlockedResourceType, List<UUID>> = emptyMap(),
)
