package com.fancia.backend.shared.common.tag.core.repository

import com.fancia.backend.shared.common.tag.core.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TagRepository : JpaRepository<Tag, UUID>
