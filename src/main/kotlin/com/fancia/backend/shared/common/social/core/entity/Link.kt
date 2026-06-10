package com.fancia.backend.shared.common.social.core.entity

import com.fancia.backend.shared.common.social.core.enums.LinkType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class Link(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var type: LinkType = LinkType.WEBSITE,

    @Column(nullable = false, length = 2048)
    var url: String = "",
)
