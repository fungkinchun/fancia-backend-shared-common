package com.fancia.backend.shared.common.core.exception

open class DomainException(
    val title: String = "Domain Error",
    override val message: String = "An error occurred while processing the request.",
    val errorCode: String? = null,
) : RuntimeException(message)