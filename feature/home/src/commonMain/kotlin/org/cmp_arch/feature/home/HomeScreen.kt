package org.cmp_arch.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.cmp_arch.core.ThemeMode
import org.cmp_arch.core.composeutils.screenHorizontalPadding
import org.cmp_arch.core.ui.ErrorState
import org.cmp_arch.core.ui.LoadingState
import org.cmp_arch.designsystem.components.DsPrimaryButton
import org.cmp_arch.designsystem.components.DsSurfaceCard
import org.cmp_arch.designsystem.components.DsTopAppBar
import org.koin.mp.KoinPlatform

@Composable
fun HomeRoute(
    onSampleRequested: () -> Unit,
) {
    val viewModel: HomeViewModel = remember {
        KoinPlatform.getKoin().get()
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbars = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToSample -> onSampleRequested()
                is HomeEffect.ShowMessage -> snackbars.showSnackbar(effect.message)
            }
        }
    }

    HomeScreen(
        state = state,
        snackbarHostState = snackbars,
        onIntent = viewModel::dispatch,
    )
}

@Composable
private fun HomeScreen(
    state: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            DsTopAppBar(
                title = "Home Template",
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DsPrimaryButton(text = "Retry") { onIntent(HomeIntent.RetryLoad) }
                        DsPrimaryButton(text = "Sample") { onIntent(HomeIntent.OpenSample) }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        when {
            state.isLoading && state.items.isEmpty() -> {
                LoadingState(modifier = Modifier.padding(contentPadding))
            }

            state.errorMessage != null && state.items.isEmpty() -> {
                ErrorState(
                    message = state.errorMessage,
                    modifier = Modifier.padding(contentPadding),
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .screenHorizontalPadding()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CounterSection(
                        count = state.counter,
                        onIncrease = { onIntent(HomeIntent.IncrementCounter) },
                        onDecrease = { onIntent(HomeIntent.DecrementCounter) },
                    )
                    ThemeSwitcherSection(
                        selectedMode = state.themeMode,
                        onSystem = { onIntent(HomeIntent.SetThemeMode(ThemeMode.SYSTEM)) },
                        onLight = { onIntent(HomeIntent.SetThemeMode(ThemeMode.LIGHT)) },
                        onDark = { onIntent(HomeIntent.SetThemeMode(ThemeMode.DARK)) },
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        item {
                            MviIntroCard()
                        }
                        items(
                            items = state.items,
                            key = { it.id },
                        ) { item ->
                            HomeTemplateItemCard(
                                item = item,
                                isSelected = state.selectedItemId == item.id,
                                onSelect = { onIntent(HomeIntent.SelectItem(item.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSwitcherSection(
    selectedMode: ThemeMode,
    onSystem: () -> Unit,
    onLight: () -> Unit,
    onDark: () -> Unit,
) {
    DsSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Current: ${selectedMode.name}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DsPrimaryButton(
                    text = "System",
                    modifier = Modifier.weight(1f),
                    onClick = onSystem,
                )
                DsPrimaryButton(
                    text = "Day",
                    modifier = Modifier.weight(1f),
                    onClick = onLight,
                )
                DsPrimaryButton(
                    text = "Night",
                    modifier = Modifier.weight(1f),
                    onClick = onDark,
                )
            }
        }
    }
}

@Composable
private fun CounterSection(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    DsSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "MVI Counter: $count",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsPrimaryButton(text = "-1", onClick = onDecrease)
                DsPrimaryButton(text = "+1", onClick = onIncrease)
            }
        }
    }
}

@Composable
private fun MviIntroCard() {
    DsSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "MVI Blueprint",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "1) UI sends Intent",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "2) ViewModel updates immutable State",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "3) One-off actions are emitted as Effect",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun HomeTemplateItemCard(
    item: HomeItemUiModel,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    DsSurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (isSelected) {
                Text(
                    text = "Selected",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
