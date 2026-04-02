package org.cmp_arch.domain.repository

import kotlinx.coroutines.flow.Flow
import org.cmp_arch.core.AppResult
import org.cmp_arch.domain.model.Article

interface ArticlesRepository {
    fun observeArticles(): Flow<List<Article>>
    suspend fun refreshArticles(): AppResult<Unit>
    suspend fun updateBookmark(articleId: String, bookmarked: Boolean): AppResult<Unit>
}
