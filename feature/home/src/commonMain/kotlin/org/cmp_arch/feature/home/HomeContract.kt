package org.cmp_arch.feature.home

import androidx.compose.runtime.Immutable

sealed interface HomeIntent {
    data object InitialLoad : HomeIntent
    data object Refresh : HomeIntent
    data class ToggleBookmark(val articleId: String, val bookmarked: Boolean) : HomeIntent
    data class OpenArticle(val articleId: String) : HomeIntent
    data object OpenSample : HomeIntent
}

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val items: List<ArticleUiModel> = emptyList(),
    val errorMessage: String? = null,
)

sealed interface HomeEffect {
    data class NavigateToArticle(val articleId: String) : HomeEffect
    data object NavigateToSample : HomeEffect
    data class ShowMessage(val message: String) : HomeEffect
}

@Immutable
data class ArticleUiModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val publishedAt: String,
    val isBookmarked: Boolean,
)
