package com.fancia.backend.shared.common.core.exception

class TagAlreadyExistsException(
    val name: String,
    message: String = "Tag $name is already existed"
) : Throwable()