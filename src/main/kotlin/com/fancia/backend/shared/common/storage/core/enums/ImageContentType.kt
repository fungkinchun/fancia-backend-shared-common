package com.fancia.backend.common.storage.core.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class ImageContentType(
    @JsonValue val mimeType: String,
    val extension: String,
) {
    JPEG("image/jpeg", "jpg"),
    PNG("image/png", "png"),
    WEBP("image/webp", "webp"),
    GIF("image/gif", "gif"),
}
