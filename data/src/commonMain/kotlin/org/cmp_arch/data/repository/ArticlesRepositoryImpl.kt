package org.cmp_arch.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.cmp_arch.core.AppResult
import org.cmp_arch.core.TemplatePreferencesStore
import org.cmp_arch.data.local.ArticlesLocalDataSource
import org.cmp_arch.data.mapper.toCacheRecord
import org.cmp_arch.data.mapper.toDomain
import org.cmp_arch.data.remote.ArticlesRemoteDataSource
import org.cmp_arch.domain.model.Article
import org.cmp_arch.domain.repository.ArticlesRepository

class ArticlesRepositoryImpl(
    private val localDataSource: ArticlesLocalDataSource,
    private val remoteDataSource: ArticlesRemoteDataSource,
    private val preferencesStore: TemplatePreferencesStore,
) : ArticlesRepository {

    override fun observeArticles(): Flow<List<Article>> {
        return localDataSource.observeArticles().map { records ->
            records.map { it.toDomain() }
        }
    }

    override suspend fun refreshArticles(): AppResult<Unit> {
        val remoteResult = remoteDataSource.getArticles()
        return when (remoteResult) {
            is AppResult.Success -> {
                val currentById = localDataSource.observeArticles().first().associateBy { it.id }
                val cacheRecords = remoteResult.value.map { dto ->
                    val previous = currentById[dto.id]
                    dto.toCacheRecord(existingBookmark = previous?.isBookmarked == true)
                }
                localDataSource.upsertArticles(cacheRecords)
                preferencesStore.writeLastSuccessfulSync(System.currentTimeMillis())
                AppResult.Success(Unit)
            }

            is AppResult.Failure -> remoteResult
        }
    }

    override suspend fun updateBookmark(articleId: String, bookmarked: Boolean): AppResult<Unit> {
        localDataSource.updateBookmark(articleId, bookmarked)
        return AppResult.Success(Unit)
    }
}
