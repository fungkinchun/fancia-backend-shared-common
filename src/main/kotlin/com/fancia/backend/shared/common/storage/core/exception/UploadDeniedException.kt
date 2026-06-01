package com.fancia.backend.common.storage.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class UploadDeniedException(
    title: String = "Upload denied",
    message: String = "You are not allowed to upload for this resource.",
    errorCode: String = "UPLOAD_DENIED",
) : DomainException(title, message, errorCode)
