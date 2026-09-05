package com.fancia.backend.shared.common.core.utils

import java.security.SecureRandom
import java.util.Base64

object InviteTokens {
    private val random = SecureRandom()
    private val encoder = Base64.getUrlEncoder().withoutPadding()

    fun generate(): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return encoder.encodeToString(bytes)
    }
}
