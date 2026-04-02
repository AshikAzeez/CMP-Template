package org.cmp_arch.data.remote

import kotlinx.coroutines.delay
import org.cmp_arch.core.AppResult

class MockArticlesRemoteDataSource : ArticlesRemoteDataSource {
    override suspend fun getArticles(): AppResult<List<ArticleDto>> {
        delay(250)
        return AppResult.Success(sampleArticles)
    }
}

private val sampleArticles = listOf(
    ArticleDto(
        id = "cmp-001",
        title = "Compose Multiplatform Template Ready",
        subtitle = "A clean baseline with modular architecture and reusable feature shells.",
        imageUrl = "https://picsum.photos/seed/cmp-1/640/360",
        publishedAtEpochMillis = 1_773_120_000_000,
    ),
    ArticleDto(
        id = "cmp-002",
        title = "MVI With Immutable State",
        subtitle = "Intent-driven updates keep UI deterministic and testable.",
        imageUrl = "https://picsum.photos/seed/cmp-2/640/360",
        publishedAtEpochMillis = 1_773_206_400_000,
    ),
    ArticleDto(
        id = "cmp-003",
        title = "Ktor + Koin + DataStore",
        subtitle = "Networking, DI, and local preferences are split by layer boundaries.",
        imageUrl = "https://picsum.photos/seed/cmp-3/640/360",
        publishedAtEpochMillis = 1_773_292_800_000,
    ),
)
