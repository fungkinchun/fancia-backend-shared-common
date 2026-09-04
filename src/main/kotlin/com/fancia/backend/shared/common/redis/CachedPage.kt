package com.fancia.backend.shared.common.redis

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

data class CachedPage<T>(
    val content: List<T> = emptyList(),
    val totalElements: Long = 0,
    val number: Int = 0,
    val size: Int = 20,
) {
    fun toPage(pageable: Pageable): Page<T> = PageImpl(content, pageable, totalElements)

    companion object {
        fun <T> from(page: Page<T>): CachedPage<T> =
            CachedPage(
                content = page.content,
                totalElements = page.totalElements,
                number = page.number,
                size = page.size,
            )
    }
}
