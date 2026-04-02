package org.cmp_arch.data.mapper

import org.cmp_arch.core.database.ArticleCacheRecord
import org.cmp_arch.data.remote.ArticleDto
import org.cmp_arch.domain.model.Article

fun ArticleDto.toCacheRecord(existingBookmark: Boolean = false): ArticleCacheRecord {
    return ArticleCacheRecord(
        id = id,
        title = title,
        subtitle = subtitle,
        imageUrl = imageUrl,
        publishedAtEpochMillis = publishedAtEpochMillis,
        isBookmarked = existingBookmark,
    )
}

fun ArticleCacheRecord.toDomain(): Article {
    return Article(
        id = id,
        title = title,
        subtitle = subtitle,
        imageUrl = imageUrl,
        publishedAtEpochMillis = publishedAtEpochMillis,
        isBookmarked = isBookmarked,
    )
}
