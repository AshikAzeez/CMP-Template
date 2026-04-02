package org.cmp_arch.feature.home

import androidx.compose.runtime.Immutable
import org.cmp_arch.core.ThemeMode

sealed interface HomeIntent {
    data object InitialLoad : HomeIntent
    data object RetryLoad : HomeIntent
    data object IncrementCounter : HomeIntent
    data object DecrementCounter : HomeIntent
    data class SelectItem(val itemId: String) : HomeIntent
    data object OpenSample : HomeIntent
    data class SetThemeMode(val mode: ThemeMode) : HomeIntent
}

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val counter: Int = 0,
    val items: List<HomeItemUiModel> = emptyList(),
    val selectedItemId: String? = null,
    val errorMessage: String? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

sealed interface HomeEffect {
    data object NavigateToSample : HomeEffect
    data class ShowMessage(val message: String) : HomeEffect
}

@Immutable
data class HomeItemUiModel(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
)
