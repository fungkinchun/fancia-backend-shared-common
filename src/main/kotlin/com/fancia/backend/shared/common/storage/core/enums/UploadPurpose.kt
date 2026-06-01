package com.fancia.backend.shared.common.storage.core.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class UploadPurpose(@JsonValue val pathSegment: String) {
    PROFILE_PICTURE("profile-picture"),
    COVER_IMAGE("cover-image"),
    IMAGE("image"),
}
