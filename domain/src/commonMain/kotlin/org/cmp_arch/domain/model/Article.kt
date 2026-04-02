package org.cmp_arch.domain.model

data class Article(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val publishedAtEpochMillis: Long,
    val isBookmarked: Boolean,
)
