package com.fancia.backend.shared.common.core.exception

class TagNotFoundException(
    val name: String,
    title: String = "Tag Already Exists",
    message: String = "Tag not found with name: $name",
    errorCode: String = "TAG_NOT_FOUND"
) : DomainException(title, message, errorCode)