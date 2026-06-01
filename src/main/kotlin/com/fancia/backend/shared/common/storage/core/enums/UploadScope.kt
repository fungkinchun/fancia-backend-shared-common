package com.fancia.backend.common.storage.core.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class UploadScope(@JsonValue val pathSegment: String) {
    USER("user"),
    EVENT("event"),
    VENUE("venue");

    fun allowedPurposes(): Set<UploadPurpose> = when (this) {
        USER -> setOf(UploadPurpose.PROFILE_PICTURE)
        EVENT -> setOf(UploadPurpose.COVER_IMAGE)
        VENUE -> setOf(UploadPurpose.IMAGE)
    }
}
