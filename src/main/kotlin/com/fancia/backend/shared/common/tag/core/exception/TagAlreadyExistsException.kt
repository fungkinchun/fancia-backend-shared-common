package com.fancia.backend.shared.common.tag.core.exception

import com.fancia.backend.shared.common.core.exception.DomainException

class TagAlreadyExistsException(
    val name: String,
    title: String = "Tag Already Exists",
    message: String = "Tag $name is already existed",
    errorCode: String = "TAG_ALREADY_EXISTS"
) : DomainException(title, message, errorCode)