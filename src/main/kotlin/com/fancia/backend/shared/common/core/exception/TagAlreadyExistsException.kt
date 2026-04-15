package com.fancia.backend.shared.common.core.exception

class TagAlreadyExistsException(
    val name: String,
    title: String = "Tag Already Exists",
    message: String = "Tag $name is already existed",
    errorCode: String = "TAG_ALREADY_EXISTS"
) : DomainException(title, message, errorCode)