package com.example.demo.http.responses.page

import kotlinx.serialization.Serializable
import org.springframework.data.domain.Page

@Serializable
data class PageResponse<T>(
    val data: List<T>,
    val meta: PageMetaDto,
) {
    constructor(page: Page<T>) : this(
        data = page.content,
        meta = PageMetaDto(
            total = page.totalElements,
            pages = page.totalPages
        )
    )
}