package org.cmp_arch.feature.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.cmp_arch.analytics.AnalyticsEvent
import org.cmp_arch.analytics.AnalyticsTracker
import org.cmp_arch.core.MviViewModel
import org.cmp_arch.core.asUiMessage
import org.cmp_arch.domain.model.Article
import org.cmp_arch.domain.usecase.ObserveArticlesUseCase
import org.cmp_arch.domain.usecase.RefreshArticlesUseCase
import org.cmp_arch.domain.usecase.UpdateBookmarkUseCase

class HomeViewModel(
    private val observeArticles: ObserveArticlesUseCase,
    private val refreshArticles: RefreshArticlesUseCase,
    private val updateBookmark: UpdateBookmarkUseCase,
    private val analyticsTracker: AnalyticsTracker,
) : MviViewModel<HomeIntent, HomeUiState, HomeEffect>(HomeUiState()) {

    private var observingJob: Job? = null

    init {
        analyticsTracker.track(AnalyticsEvent(name = "home_opened"))
        dispatch(HomeIntent.InitialLoad)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InitialLoad -> onInitialLoad()
            is HomeIntent.Refresh -> refresh()
            is HomeIntent.ToggleBookmark -> toggleBookmark(intent.articleId, intent.bookmarked)
            is HomeIntent.OpenArticle -> {
                analyticsTracker.track(
                    AnalyticsEvent(
                        name = "article_open_requested",
                        parameters = mapOf("articleId" to intent.articleId),
                    ),
                )
                postEffect(HomeEffect.NavigateToArticle(intent.articleId))
            }
            is HomeIntent.OpenSample -> {
                analyticsTracker.track(AnalyticsEvent(name = "sample_open_requested"))
                postEffect(HomeEffect.NavigateToSample)
            }
        }
    }

    private fun onInitialLoad() {
        if (observingJob == null) {
            observingJob = observeArticles()
                .onEach { articles ->
                    setState { current ->
                        current.copy(
                            isLoading = false,
                            items = articles.map(Article::toUiModel),
                            errorMessage = null,
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true, errorMessage = null) }
            when (val result = refreshArticles()) {
                is org.cmp_arch.core.AppResult.Success -> {
                    setState { it.copy(isLoading = false) }
                }

                is org.cmp_arch.core.AppResult.Failure -> {
                    val message = result.error.asUiMessage()
                    setState { it.copy(isLoading = false, errorMessage = message) }
                    postEffect(HomeEffect.ShowMessage(message))
                }
            }
        }
    }

    private fun toggleBookmark(articleId: String, bookmarked: Boolean) {
        viewModelScope.launch {
            updateBookmark(articleId, bookmarked)
        }
    }
}

private fun Article.toUiModel(): ArticleUiModel {
    return ArticleUiModel(
        id = id,
        title = title,
        subtitle = subtitle,
        imageUrl = imageUrl,
        publishedAt = publishedAtEpochMillis.toString(),
        isBookmarked = isBookmarked,
    )
}
