package org.cmp_arch.core.database

import kotlinx.coroutines.flow.Flow

/**
 * Cache contract used by data layer. Domain does not know this exists.
 */
interface ArticleCache {
    fun observeAll(): Flow<List<ArticleCacheRecord>>
    suspend fun upsert(records: List<ArticleCacheRecord>)
    suspend fun updateBookmark(articleId: String, bookmarked: Boolean)
}

data class ArticleCacheRecord(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val publishedAtEpochMillis: Long,
    val isBookmarked: Boolean,
)
