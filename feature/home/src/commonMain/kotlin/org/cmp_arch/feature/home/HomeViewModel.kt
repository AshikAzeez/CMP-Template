package org.cmp_arch.feature.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.cmp_arch.analytics.AnalyticsEvent
import org.cmp_arch.analytics.AnalyticsTracker
import org.cmp_arch.core.AppResult
import org.cmp_arch.core.MviViewModel
import org.cmp_arch.core.ThemeSettingsStore
import org.cmp_arch.core.asUiMessage
import org.cmp_arch.domain.model.HomeTemplateItem
import org.cmp_arch.domain.model.ItemStatus
import org.cmp_arch.domain.usecase.ObserveHomeTemplateItemsUseCase
import org.cmp_arch.domain.usecase.SeedHomeTemplateItemsUseCase

class HomeViewModel(
    private val observeItems: ObserveHomeTemplateItemsUseCase,
    private val seedItems: SeedHomeTemplateItemsUseCase,
    private val analyticsTracker: AnalyticsTracker,
    private val themeSettingsStore: ThemeSettingsStore,
) : MviViewModel<HomeIntent, HomeUiState, HomeEffect>(HomeUiState()) {

    private var collectionJob: Job? = null
    private var themeJob: Job? = null

    init {
        analyticsTracker.track(AnalyticsEvent(name = "home_opened"))
        observeThemeMode()
        dispatch(HomeIntent.InitialLoad)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.InitialLoad -> onInitialLoad()
            is HomeIntent.RetryLoad -> onRetryLoad()
            is HomeIntent.IncrementCounter -> changeCounter(1)
            is HomeIntent.DecrementCounter -> changeCounter(-1)
            is HomeIntent.SelectItem -> onSelectItem(intent.itemId)
            is HomeIntent.OpenSample -> {
                analyticsTracker.track(AnalyticsEvent(name = "sample_open_requested"))
                postEffect(HomeEffect.NavigateToSample)
            }
            is HomeIntent.SetThemeMode -> onSetThemeMode(intent)
        }
    }

    private fun onInitialLoad() {
        if (collectionJob == null) {
            collectionJob = observeItems()
                .onEach { items ->
                    setState { current ->
                        current.copy(
                            isLoading = false,
                            items = items.map(HomeTemplateItem::toUiModel),
                            errorMessage = null,
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
        seedIfNeeded()
    }

    private fun onRetryLoad() {
        seedIfNeeded()
    }

    private fun seedIfNeeded() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true, errorMessage = null) }
            when (val result = seedItems()) {
                is AppResult.Success -> setState { it.copy(isLoading = false) }
                is AppResult.Failure -> {
                    val message = result.error.asUiMessage()
                    setState { it.copy(isLoading = false, errorMessage = message) }
                    postEffect(HomeEffect.ShowMessage(message))
                }
            }
        }
    }

    private fun observeThemeMode() {
        if (themeJob != null) return
        themeJob = themeSettingsStore.themeMode
            .onEach { mode ->
                setState { it.copy(themeMode = mode) }
            }
            .launchIn(viewModelScope)
    }

    private fun onSetThemeMode(intent: HomeIntent.SetThemeMode) {
        viewModelScope.launch {
            themeSettingsStore.setThemeMode(intent.mode)
            analyticsTracker.track(
                AnalyticsEvent(
                    name = "theme_mode_selected",
                    parameters = mapOf("mode" to intent.mode.name),
                ),
            )
        }
    }

    private fun changeCounter(delta: Int) {
        setState { current ->
            current.copy(counter = (current.counter + delta).coerceAtLeast(0))
        }
    }

    private fun onSelectItem(itemId: String) {
        val selected = state.value.items.firstOrNull { it.id == itemId } ?: return
        analyticsTracker.track(
            AnalyticsEvent(
                name = "home_item_selected",
                parameters = mapOf("itemId" to itemId),
            ),
        )
        setState { current -> current.copy(selectedItemId = itemId) }
        postEffect(HomeEffect.ShowMessage("Selected: ${selected.title}"))
    }
}

private fun HomeTemplateItem.toUiModel(): HomeItemUiModel {
    return HomeItemUiModel(
        id = id,
        title = title,
        description = description,
        status = when (status) {
            ItemStatus.TODO -> "TODO"
            ItemStatus.IN_PROGRESS -> "IN PROGRESS"
            ItemStatus.DONE -> "DONE"
        },
    )
}
