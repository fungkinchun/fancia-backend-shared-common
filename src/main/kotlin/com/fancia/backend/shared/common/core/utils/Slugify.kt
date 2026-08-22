package com.fancia.backend.shared.common.core.utils

import java.util.UUID

object Slugify {
    private const val MAX_BASE_LENGTH = 80
    private val NON_ALNUM = Regex("[^a-z0-9]+")
    private val MULTI_HYPHEN = Regex("-{2,}")

    fun slugify(raw: String, fallback: String = "item"): String {
        val base = raw
            .lowercase()
            .replace(NON_ALNUM, "-")
            .replace(MULTI_HYPHEN, "-")
            .trim('-')
            .take(MAX_BASE_LENGTH)
            .trim('-')
        return base.ifBlank { fallback }
    }

    fun allocateUnique(base: String, fallback: String = "item", exists: (String) -> Boolean): String {
        val root = slugify(base, fallback)
        if (!exists(root)) return root
        for (n in 2..100) {
            val candidate = "$root-$n"
            if (!exists(candidate)) return candidate
        }
        return "$root-${UUID.randomUUID().toString().replace("-", "").take(8)}"
    }
}
