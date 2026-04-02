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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.cmp_arch.core.composeutils.screenHorizontalPadding
import org.cmp_arch.core.ui.ErrorState
import org.cmp_arch.core.ui.LoadingState
import org.cmp_arch.designsystem.components.DsPrimaryButton
import org.cmp_arch.designsystem.components.DsSurfaceCard
import org.cmp_arch.designsystem.components.DsTopAppBar
import org.koin.core.context.GlobalContext

@Composable
fun HomeRoute(
    onArticleRequested: (String) -> Unit,
    onSampleRequested: () -> Unit,
) {
    val viewModel: HomeViewModel = remember {
        GlobalContext.get().get()
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbars = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToArticle -> onArticleRequested(effect.articleId)
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
                title = "News",
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DsPrimaryButton(text = "Sample") { onIntent(HomeIntent.OpenSample) }
                        DsPrimaryButton(text = "Refresh") { onIntent(HomeIntent.Refresh) }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .screenHorizontalPadding()
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = state.items,
                        key = { it.id },
                    ) { article ->
                        ArticleRow(
                            article = article,
                            onOpen = { onIntent(HomeIntent.OpenArticle(article.id)) },
                            onToggleBookmark = {
                                onIntent(HomeIntent.ToggleBookmark(article.id, !article.isBookmarked))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleRow(
    article: ArticleUiModel,
    onOpen: () -> Unit,
    onToggleBookmark: () -> Unit,
) {
    DsSurfaceCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth(),
            )

            Column(
                modifier = Modifier.weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = article.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = article.publishedAt,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    IconButton(onClick = onToggleBookmark) {
                        Text(if (article.isBookmarked) "Saved" else "Save")
                    }
                }
            }
        }
    }
}
