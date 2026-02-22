package com.fancia.backend.shared.common.core.exception

class TagNotFoundException(
    val name: String,
    message: String = "Tag not found with name: $name"
) : Throwable()