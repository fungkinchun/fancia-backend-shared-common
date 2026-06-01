package com.fancia.backend.common.storage.core.dto

import java.time.Instant

data class PresignUploadResponse(
    val uploadUrl: String,
    val objectKey: String,
    val publicUrl: String,
    val expiresAt: Instant,
)
