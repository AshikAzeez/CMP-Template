package org.cmp_arch.data.local

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.core.database.ArticleCache
import org.cmp_arch.core.database.ArticleCacheRecord

class ArticlesLocalDataSource(
    private val articleCache: ArticleCache,
) {
    fun observeArticles(): Flow<List<ArticleCacheRecord>> = articleCache.observeAll()

    suspend fun upsertArticles(records: List<ArticleCacheRecord>) {
        articleCache.upsert(records)
    }

    suspend fun updateBookmark(articleId: String, bookmarked: Boolean) {
        articleCache.updateBookmark(articleId, bookmarked)
    }
}
