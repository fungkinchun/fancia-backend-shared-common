package com.fancia.backend.shared.common.tag.core.entity

import com.fancia.backend.shared.common.core.entity.AbstractEntity
import com.fancia.backend.shared.common.tag.core.enums.TagType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "tags",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["name", "type"]),
    ],
)
class Tag(
    @Column(nullable = false, length = 50)
    var name: String = "",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    var type: TagType = TagType.INTEREST,
) : AbstractEntity()
