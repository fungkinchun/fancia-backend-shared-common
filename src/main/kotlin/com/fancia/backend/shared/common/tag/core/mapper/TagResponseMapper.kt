package com.fancia.backend.shared.common.tag.core.mapper

import com.fancia.backend.shared.common.tag.core.dto.TagResponse
import com.fancia.backend.shared.common.tag.core.entity.Tag
import com.fancia.backend.shared.common.tag.core.repository.TagRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TagResponseMapper(
    private val tagRepository: TagRepository,
) {
    fun fromIds(ids: Collection<UUID>): Set<TagResponse> {
        if (ids.isEmpty()) return emptySet()
        val byId = tagRepository.findAllById(ids).associateBy { it.id }
        return ids.mapNotNull { id -> byId[id]?.toResponse() }.toSet()
    }
}

private fun Tag.toResponse(): TagResponse =
    TagResponse(
        id = id,
        name = name,
        type = type,
        createdBy = createdBy,
        createdAt = createdAt,
    )
