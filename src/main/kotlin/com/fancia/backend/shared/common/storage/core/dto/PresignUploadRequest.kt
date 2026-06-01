package com.fancia.backend.shared.common.storage.core.dto

import com.fancia.backend.shared.common.storage.core.enums.ImageContentType
import com.fancia.backend.shared.common.storage.core.enums.UploadPurpose
import com.fancia.backend.shared.common.storage.core.enums.UploadScope
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class PresignUploadRequest(
    @field:NotNull
    val scope: UploadScope,
    @field:NotNull
    val purpose: UploadPurpose,
    val resourceId: UUID? = null,
    @field:NotNull
    val contentType: ImageContentType,
    @field:NotBlank
    val filename: String,
)
