package com.fancia.backend.shared.common.post.core.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class PostMediaType(@JsonValue val value: String) {
    IMAGE("image"),
    VIDEO("video"),
}
