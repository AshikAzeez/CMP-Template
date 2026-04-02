package org.cmp_arch.core.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.cmp_arch.core.PlatformContext

private class InMemoryArticleCache : BaseRoomStore<ArticleCacheRecord>(), ArticleCache {
    private val state = MutableStateFlow<List<ArticleCacheRecord>>(emptyList())

    override fun observeAll(): Flow<List<ArticleCacheRecord>> = state.asStateFlow()

    override suspend fun upsertAll(records: List<ArticleCacheRecord>) {
        val indexed = state.value.associateBy { it.id }.toMutableMap()
        records.forEach { record ->
            indexed[record.id] = record
        }
        state.value = indexed.values.sortedByDescending { it.publishedAtEpochMillis }
    }

    override suspend fun updateOne(recordId: String, block: (ArticleCacheRecord) -> ArticleCacheRecord) {
        state.value = state.value.map { record ->
            if (record.id == recordId) {
                block(record)
            } else {
                record
            }
        }
    }

    override suspend fun upsert(records: List<ArticleCacheRecord>) = upsertAll(records)

    override suspend fun updateBookmark(articleId: String, bookmarked: Boolean) {
        updateOne(articleId) { current -> current.copy(isBookmarked = bookmarked) }
    }
}

actual class DatabaseFactory actual constructor(
    context: PlatformContext,
) {
    private val migrationRegistry = MigrationRegistry(
        schemaVersion = 3,
        migrations = listOf(
            DatabaseMigration(1, 2, "Add publishedAtEpochMillis column"),
            DatabaseMigration(2, 3, "Add isBookmarked column with default false"),
        ),
    ).also { it.validate() }

    private val sharedCache = InMemoryArticleCache()

    actual fun createArticleCache(): ArticleCache = sharedCache
}
